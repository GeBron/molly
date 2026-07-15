<script setup lang="ts">
import type { MenuVO } from '@/types'

defineProps<{
  menus: MenuVO[]
}>()
</script>

<template>
  <template v-for="menu in menus" :key="menu.id">
    <el-sub-menu
      v-if="menu.children && menu.children.length"
      :index="menu.path || String(menu.id)"
    >
      <template #title>
        <span>{{ menu.name }}</span>
      </template>
      <SideMenu :menus="menu.children" />
    </el-sub-menu>
    <el-menu-item v-else :index="menu.path || String(menu.id)">
      <span>{{ menu.name }}</span>
    </el-menu-item>
  </template>
</template>
