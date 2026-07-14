<script setup lang="ts">
import { useAuth } from '@/composables/useAuth'

const { userInfo } = useAuth()
</script>

<template>
  <div class="space-y-6">
    <el-card>
      <h1 class="text-xl font-bold mb-2">欢迎使用 Molly Admin</h1>
      <p class="text-gray-600">当前用户：{{ userInfo?.user.username }}</p>
      <p class="text-gray-600">角色：{{ userInfo?.roles.join(', ') || '-' }}</p>
    </el-card>

    <el-row :gutter="16">
      <el-col :span="8">
        <el-card class="text-center">
          <div class="text-3xl font-bold text-blue-500">RBAC</div>
          <div class="text-gray-500 mt-1">基于角色的权限控制</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="text-center">
          <div class="text-3xl font-bold text-green-500">JWT</div>
          <div class="text-gray-500 mt-1">长时效 Token 认证</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="text-center">
          <div class="text-3xl font-bold text-purple-500">AOP</div>
          <div class="text-gray-500 mt-1">登录与操作日志记录</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
