<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import * as logApi from '@/api/log'
import type { LogQueryDTO, LoginLogVO } from '@/types'

const loading = ref(false)
const list = ref<LoginLogVO[]>([])
const total = ref(0)
const query = reactive<LogQueryDTO>({ pageNum: 1, pageSize: 10 })
const dateRange = ref<[Date, Date] | null>(null)

async function fetchData() {
  loading.value = true
  try {
    const params: LogQueryDTO = { ...query }
    if (dateRange.value) {
      params.startTime = dateRange.value[0].toISOString()
      params.endTime = dateRange.value[1].toISOString()
    }
    const res = await logApi.loginLogs(params)
    if (res.code === 200 && res.data) {
      list.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.pageNum = 1
  dateRange.value = null
  fetchData()
}

onMounted(fetchData)
</script>

<template>
  <div class="space-y-4">
    <el-card>
      <el-form :inline="true" class="flex flex-wrap gap-y-2">
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="list" border stripe>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="ip" label="IP" />
        <el-table-column prop="operation" label="操作" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'SUCCESS' ? 'success' : 'danger'">{{ row.status === 'SUCCESS' ? '成功' : '失败' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="message" label="消息" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="时间" min-width="160" />
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next"
          :page-sizes="[10, 20, 50]"
          @change="fetchData"
        />
      </div>
    </el-card>
  </div>
</template>
