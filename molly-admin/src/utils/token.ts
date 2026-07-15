import { ref } from 'vue'

const accessToken = ref<string>('')

export function getAccessToken(): string {
  return accessToken.value
}

export function setAccessToken(token: string): void {
  accessToken.value = token
}

export function clearAccessToken(): void {
  accessToken.value = ''
}

export { accessToken }
