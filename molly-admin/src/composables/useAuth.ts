import { ref, computed } from 'vue'
import router from '@/router'
import * as authApi from '@/api/auth'
import type { UserInfoVO } from '@/types'

const token = ref<string>(localStorage.getItem('token') || '')
const userInfo = ref<UserInfoVO | null>(null)

export function useAuth() {
  const isLoggedIn = computed(() => !!token.value)

  async function login(username: string, password: string) {
    const res = await authApi.login({ username, password })
    if (res.code === 200 && res.data) {
      token.value = res.data.token
      localStorage.setItem('token', res.data.token)
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
    }
  }

  function logout() {
    authApi.logout().catch(() => {})
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    router.push('/login')
  }

  function clearAuth() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    loadUserInfo,
    logout,
    clearAuth,
  }
}
