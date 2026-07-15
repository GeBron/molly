<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePermission } from '@/composables/usePermission'
import * as userApi from '@/api/user'
import * as roleApi from '@/api/role'
import type { RoleVO, UserDTO, UserQueryDTO, UserVO } from '@/types'

const { hasPermission } = usePermission()

const loading = ref(false)
const list = ref<UserVO[]>([])
const total = ref(0)
const query = reactive<UserQueryDTO>({ pageNum: 1, pageSize: 10 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive<UserDTO & { id?: number }>({ username: '', password: '', realName: '', status: 1 })
const formRef = ref()

const roleDialogVisible = ref(false)
const currentUser = ref<UserVO | null>(null)
const roles = ref<RoleVO[]>([])
const selectedRoleIds = ref<number[]>([])

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function fetchData() {
  loading.value = true
  try {
    const res = await userApi.listUsers(query)
    if (res.code === 200 && res.data) {
      list.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.username = undefined
  query.status = undefined
  query.pageNum = 1
  fetchData()
}

function openCreate() {
  dialogTitle.value = '新增用户'
  form.id = undefined
  form.username = ''
  form.password = ''
  form.realName = ''
  form.status = 1
  dialogVisible.value = true
}

function openEdit(row: UserVO) {
  dialogTitle.value = '编辑用户'
  form.id = row.id
  form.username = row.username
  form.password = ''
  form.realName = row.realName
  form.status = row.status
  dialogVisible.value = true
}

async function save() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  let res
  if (form.id) {
    res = await userApi.updateUser(form.id, { realName: form.realName, status: form.status })
  } else {
    res = await userApi.createUser({ username: form.username, password: form.password, realName: form.realName, status: form.status })
  }
  if (res.code === 200) {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } else {
    ElMessage.error(res.message || '保存失败')
  }
}

async function remove(row: UserVO) {
  try {
    await ElMessageBox.confirm('确认删除该用户吗？', '提示', { type: 'warning' })
    const res = await userApi.deleteUser(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch {
    // cancel
  }
}

async function openAssignRoles(row: UserVO) {
  currentUser.value = row
  selectedRoleIds.value = row.roleIds || []
  const res = await roleApi.listRoles({ pageNum: 1, pageSize: 999 })
  roles.value = res.data?.list || []
  roleDialogVisible.value = true
}

async function saveAssignRoles() {
  if (!currentUser.value) return
  const res = await userApi.assignUserRoles(currentUser.value.id, { roleIds: selectedRoleIds.value })
  if (res.code === 200) {
    ElMessage.success('分配成功')
    roleDialogVisible.value = false
    fetchData()
  } else {
    ElMessage.error(res.message || '分配失败')
  }
}

async function toggleStatus(row: UserVO) {
  const status = row.status === 1 ? 0 : 1
  const res = await userApi.updateUserStatus(row.id, status)
  if (res.code === 200) {
    ElMessage.success('状态更新成功')
    fetchData()
  } else {
    ElMessage.error(res.message || '状态更新失败')
  }
}

onMounted(fetchData)
</script>

<template>
  <div class="space-y-4">
    <el-card>
      <el-form :inline="true" :model="query" class="flex flex-wrap gap-y-2">
        <el-form-item label="用户名">
          <el-input v-model="query.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button v-if="hasPermission('user:create')" type="success" @click="openCreate">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="list" border stripe>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="真实姓名" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roleNames" label="角色">
          <template #default="{ row }">
            {{ row.roleNames?.join(', ') || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button v-if="hasPermission('user:update')" type="primary" size="small" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="hasPermission('user:update')" size="small" @click="toggleStatus(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
            <el-button v-if="hasPermission('user:create')" size="small" @click="openAssignRoles(row)">分配角色</el-button>
            <el-button v-if="hasPermission('user:delete')" type="danger" size="small" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item v-if="!form.id" label="密码" prop="password">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="roleDialogVisible" title="分配角色" width="500px">
      <el-select v-model="selectedRoleIds" multiple placeholder="请选择角色" class="w-full">
        <el-option v-for="role in roles" :key="role.id" :label="role.roleName" :value="role.id" />
      </el-select>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAssignRoles">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
