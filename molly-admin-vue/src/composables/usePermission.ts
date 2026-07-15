import { computed } from 'vue'
import { useAuth } from './useAuth'

export function usePermission() {
  const { userInfo } = useAuth()
  const permissions = computed(() => userInfo.value?.permissions || [])

  function hasPermission(perm: string) {
    return permissions.value.includes(perm)
  }

  return { hasPermission }
}
