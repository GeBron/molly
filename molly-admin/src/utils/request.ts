import axios from 'axios'
import type { Result } from '@/types'
import { getAccessToken, setAccessToken, clearAccessToken } from './token'

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  withCredentials: true,
})

let isRefreshing = false
let refreshSubscribers: ((token: string) => void)[] = []

function onRefreshed(token: string): void {
  refreshSubscribers.forEach((cb) => cb(token))
  refreshSubscribers = []
}

function addRefreshSubscriber(cb: (token: string) => void): void {
  refreshSubscribers.push(cb)
}

async function doRefresh(): Promise<string> {
  const res = await axios.post<Result<{ token: string; tokenType: string; expiresIn: number }>>(
    '/auth/refresh',
    {},
    {
      baseURL: request.defaults.baseURL,
      timeout: 10000,
      withCredentials: true,
    }
  )
  if (res.data.code === 200 && res.data.data) {
    setAccessToken(res.data.data.token)
    return res.data.data.token
  }
  throw new Error(res.data.message || '刷新失败')
}

request.interceptors.request.use((config) => {
  const token = getAccessToken()
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => response.data,
  async (error) => {
    const originalRequest = error.config as any
    if (
      error.response?.status === 401 &&
      !originalRequest._retry &&
      !originalRequest.url?.includes('/auth/refresh')
    ) {
      if (isRefreshing) {
        return new Promise((resolve) => {
          addRefreshSubscriber((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`
            resolve(request(originalRequest))
          })
        })
      }

      originalRequest._retry = true
      isRefreshing = true
      try {
        const token = await doRefresh()
        onRefreshed(token)
        originalRequest.headers.Authorization = `Bearer ${token}`
        return request(originalRequest)
      } catch {
        clearAccessToken()
        window.location.href = '/login'
        return Promise.reject(error)
      } finally {
        isRefreshing = false
      }
    }
    return Promise.reject(error)
  }
)

export default request

export function get<T>(url: string, params?: object): Promise<Result<T>> {
  return request.get(url, { params }) as Promise<Result<T>>
}

export function post<T>(url: string, data?: object): Promise<Result<T>> {
  return request.post(url, data) as Promise<Result<T>>
}

export function put<T>(url: string, data?: object, config?: object): Promise<Result<T>> {
  return request.put(url, data, config) as Promise<Result<T>>
}

export function del<T>(url: string): Promise<Result<T>> {
  return request.delete(url) as Promise<Result<T>>
}
