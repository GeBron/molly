<script setup lang="ts">
import { ref } from 'vue'
import { useAuth } from '@/composables/useAuth'

const { login } = useAuth()
const form = ref({ username: 'admin', password: 'admin123' })
const loading = ref(false)
const errorMsg = ref('')

async function submit() {
  if (!form.value.username || !form.value.password) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  errorMsg.value = ''
  try {
    await login(form.value.username, form.value.password)
  } catch (e: any) {
    errorMsg.value = e.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-800 to-slate-900">
    <el-card class="w-full max-w-md shadow-2xl">
      <h2 class="text-2xl font-bold text-center mb-6 text-gray-800">Molly Admin</h2>
      <el-form :model="form" label-position="top" @submit.prevent="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-button type="primary" class="w-full" :loading="loading" native-type="submit">
          登录
        </el-button>
        <p v-if="errorMsg" class="text-red-500 text-sm mt-3 text-center">{{ errorMsg }}</p>
      </el-form>
    </el-card>
  </div>
</template>
