import axios from 'axios'
import type { Result } from '@/types'

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
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
