import type { Directive } from 'vue'
import { usePermission } from '@/composables/usePermission'

export const vPermission: Directive<HTMLElement, string> = {
  mounted(el, binding) {
    const { hasPermission } = usePermission()
    if (!hasPermission(binding.value)) {
      el.remove()
    }
  },
}
