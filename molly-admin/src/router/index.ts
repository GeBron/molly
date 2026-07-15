import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import AppLayout from '@/components/AppLayout.vue'
import LoginPage from '@/pages/LoginPage.vue'
import DashboardPage from '@/pages/DashboardPage.vue'
import UserPage from '@/pages/UserPage.vue'
import RolePage from '@/pages/RolePage.vue'
import PermissionPage from '@/pages/PermissionPage.vue'
import LoginLogPage from '@/pages/LoginLogPage.vue'
import OperationLogPage from '@/pages/OperationLogPage.vue'

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
    children: [
      { path: 'dashboard', component: DashboardPage, meta: { title: '首页' } },
      { path: 'system/user', component: UserPage, meta: { title: '用户管理' } },
      { path: 'system/role', component: RolePage, meta: { title: '角色管理' } },
      { path: 'system/permission', component: PermissionPage, meta: { title: '权限管理' } },
      { path: 'system/login-log', component: LoginLogPage, meta: { title: '登录日志' } },
      { path: 'system/operation-log', component: OperationLogPage, meta: { title: '操作日志' } },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (!to.meta.public && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
