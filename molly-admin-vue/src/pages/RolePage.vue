<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePermission } from '@/composables/usePermission'
import * as roleApi from '@/api/role'
import * as permissionApi from '@/api/permission'
import type { PermissionVO, RoleDTO, RoleQueryDTO, RoleVO } from '@/types'

const { hasPermission } = usePermission()

const loading = ref(false)
const list = ref<RoleVO[]>([])
const total = ref(0)
const query = reactive<RoleQueryDTO>({ pageNum: 1, pageSize: 10 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive<RoleDTO & { id?: number }>({ roleCode: '', roleName: '', status: 1 })
const formRef = ref()

const permDialogVisible = ref(false)
const currentRole = ref<RoleVO | null>(null)
const permissions = ref<PermissionVO[]>([])
const treeRef = ref()

const rules = {
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
}

const treeProps = { label: 'permName', children: 'children' }

async function fetchData() {
  loading.value = true
  try {
    const res = await roleApi.listRoles(query)
    if (res.code === 200 && res.data) {
      list.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.roleName = undefined
  query.status = undefined
  query.pageNum = 1
  fetchData()
}

function openCreate() {
  dialogTitle.value = '新增角色'
  form.id = undefined
  form.roleCode = ''
  form.roleName = ''
  form.status = 1
  dialogVisible.value = true
}

function openEdit(row: RoleVO) {
  dialogTitle.value = '编辑角色'
  form.id = row.id
  form.roleCode = row.roleCode
  form.roleName = row.roleName
  form.status = row.status
  dialogVisible.value = true
}

async function save() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  const res = form.id ? await roleApi.updateRole(form.id, form) : await roleApi.createRole(form)
  if (res.code === 200) {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } else {
    ElMessage.error(res.message || '保存失败')
  }
}

async function remove(row: RoleVO) {
  try {
    await ElMessageBox.confirm('确认删除该角色吗？', '提示', { type: 'warning' })
    const res = await roleApi.deleteRole(row.id)
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

async function toggleStatus(row: RoleVO) {
  const status = row.status === 1 ? 0 : 1
  const res = await roleApi.updateRoleStatus(row.id, status)
  if (res.code === 200) {
    ElMessage.success('状态更新成功')
    fetchData()
  } else {
    ElMessage.error(res.message || '状态更新失败')
  }
}

async function openAssignPermissions(row: RoleVO) {
  currentRole.value = row
  const res = await permissionApi.permissionTree()
  permissions.value = res.data || []
  permDialogVisible.value = true
  setTimeout(() => {
    treeRef.value?.setCheckedKeys(row.permissionIds || [], false)
  }, 0)
}

async function saveAssignPermissions() {
  if (!currentRole.value) return
  const keys = treeRef.value?.getCheckedKeys(false) as number[]
  const halfKeys = treeRef.value?.getHalfCheckedKeys(false) as number[]
  const permissionIds = [...keys, ...halfKeys]
  const res = await roleApi.assignRolePermissions(currentRole.value.id, { permissionIds })
  if (res.code === 200) {
    ElMessage.success('分配成功')
    permDialogVisible.value = false
    fetchData()
  } else {
    ElMessage.error(res.message || '分配失败')
  }
}

onMounted(fetchData)
</script>

<template>
  <div class="space-y-4">
    <el-card>
      <el-form :inline="true" :model="query" class="flex flex-wrap gap-y-2">
        <el-form-item label="角色名称">
          <el-input v-model="query.roleName" placeholder="请输入角色名称" clearable />
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
          <el-button v-if="hasPermission('role:create')" type="success" @click="openCreate">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="list" border stripe>
        <el-table-column prop="roleCode" label="角色编码" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button v-if="hasPermission('role:update')" type="primary" size="small" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="hasPermission('role:update')" size="small" @click="toggleStatus(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
            <el-button v-if="hasPermission('role:assign-perm')" size="small" @click="openAssignPermissions(row)">分配权限</el-button>
            <el-button v-if="hasPermission('role:delete')" type="danger" size="small" @click="remove(row)">删除</el-button>
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
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" />
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

    <el-dialog v-model="permDialogVisible" title="分配权限" width="500px">
      <el-tree
        ref="treeRef"
        :data="permissions"
        :props="treeProps"
        node-key="id"
        show-checkbox
        default-expand-all
      />
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAssignPermissions">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
