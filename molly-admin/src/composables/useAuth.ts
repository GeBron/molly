import { computed } from 'vue'
import router from '@/router'
import * as authApi from '@/api/auth'
import type { UserInfoVO } from '@/types'
import { accessToken, setAccessToken, clearAccessToken } from '@/utils/token'

const userInfo = ref<UserInfoVO | null>(null)

export function useAuth() {
  const isLoggedIn = computed(() => !!accessToken.value)

  async function login(username: string, password: string) {
    const res = await authApi.login({ username, password })
    if (res.code === 200 && res.data) {
      setAccessToken(res.data.token)
      await loadUserInfo()
      router.push('/')
    } else {
      throw new Error(res.message || '登录失败')
    }
  }

  async function loadUserInfo() {
    const res = await authApi.info()
    if (res.code === 200 && res.data) {
      userInfo.value = res.data
      const { generateRoutes } = await import('@/router')
      generateRoutes(res.data.menus)
    }
  }

  function logout() {
    authApi.logout().catch(() => {})
    clearAccessToken()
    userInfo.value = null
    router.push('/login')
  }

  function clearAuth() {
    clearAccessToken()
    userInfo.value = null
  }

  return {
    accessToken,
    userInfo,
    isLoggedIn,
    login,
    loadUserInfo,
    logout,
    clearAuth,
  }
}
