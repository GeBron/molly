import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import AppLayout from '@/components/AppLayout.vue'
import LoginPage from '@/pages/LoginPage.vue'
import Empty from '@/components/Empty.vue'
import type { MenuVO } from '@/types'
import { getAccessToken, setAccessToken, clearAccessToken } from '@/utils/token'
import * as authApi from '@/api/auth'

const componentMap: Record<string, () => Promise<any>> = {
  dashboard: () => import('@/pages/DashboardPage.vue'),
  'system/user': () => import('@/pages/UserPage.vue'),
  'system/role': () => import('@/pages/RolePage.vue'),
  'system/permission': () => import('@/pages/PermissionPage.vue'),
  'system/login-log': () => import('@/pages/LoginLogPage.vue'),
  'system/operation-log': () => import('@/pages/OperationLogPage.vue'),
}

function generateRouteRecords(menus: MenuVO[]): RouteRecordRaw[] {
  const routes: RouteRecordRaw[] = []
  function walk(list: MenuVO[]) {
    for (const menu of list) {
      if (menu.children && menu.children.length > 0) {
        walk(menu.children)
      } else if (menu.path) {
        const path = menu.path.startsWith('/') ? menu.path.slice(1) : menu.path
        const loader = componentMap[path]
        routes.push({
          path,
          component: loader || Empty,
          meta: { title: menu.name, permCode: menu.permCode },
        })
      }
    }
  }
  walk(menus)
  return routes
}

let routesGenerated = false

export function generateRoutes(menus: MenuVO[]) {
  const routes = generateRouteRecords(menus)
  routes.forEach((route) => router.addRoute('/', route))
  routesGenerated = true
  return routes
}

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
    meta: { public: true },
  },
  {
    path: '/',
    component: AppLayout,
    redirect: '/dashboard',
    children: [],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to, _from, next) => {
  if (to.meta.public) {
    if (to.path === '/login' && getAccessToken()) {
      return next('/')
    }
    return next()
  }

  if (!getAccessToken()) {
    try {
      const res = await authApi.refresh()
      if (res.code === 200 && res.data) {
        setAccessToken(res.data.token)
      } else {
        throw new Error(res.message || '刷新失败')
      }
    } catch {
      clearAccessToken()
      return next('/login')
    }
  }

  if (!routesGenerated) {
    try {
      const res = await authApi.info()
      if (res.code === 200 && res.data) {
        generateRoutes(res.data.menus)
      } else {
        throw new Error(res.message || '获取用户信息失败')
      }
    } catch {
      clearAccessToken()
      return next('/login')
    }
  }

  if (to.matched.length === 0) {
    return next({ ...to, replace: true })
  }

  next()
})

export default router
