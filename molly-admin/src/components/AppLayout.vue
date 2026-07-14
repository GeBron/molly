<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuth } from '@/composables/useAuth'
import SideMenu from './SideMenu.vue'

const route = useRoute()
const { userInfo, logout } = useAuth()
const activeMenu = computed(() => route.path)
const pageTitle = computed(() => (route.meta.title as string) || '')
</script>

<template>
  <el-container class="h-screen">
    <el-aside width="220px" class="bg-slate-900 text-white">
      <div class="h-14 flex items-center justify-center text-lg font-bold border-b border-slate-700">
        Molly Admin
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#0f172a"
        text-color="#cbd5e1"
        active-text-color="#ffffff"
        class="border-r-0"
      >
        <SideMenu :menus="userInfo?.menus || []" />
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="flex items-center justify-between bg-white shadow-sm z-10">
        <div class="text-gray-700 font-medium">{{ pageTitle }}</div>
        <div class="flex items-center gap-4">
          <span class="text-sm text-gray-600">{{ userInfo?.user.username }}</span>
          <el-button type="primary" size="small" @click="logout">退出</el-button>
        </div>
      </el-header>

      <el-main class="bg-gray-50">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
