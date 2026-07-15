<script setup lang="ts">
import { ref, onMounted, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePermission } from '@/composables/usePermission'
import * as permissionApi from '@/api/permission'
import type { PermissionDTO, PermissionVO } from '@/types'

const { hasPermission } = usePermission()

const loading = ref(false)
const list = ref<PermissionVO[]>([])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive<PermissionDTO & { id?: number }>({
  permCode: '',
  permName: '',
  type: 2,
  parentId: 0,
  path: '',
  sort: 0,
  status: 1,
})
const formRef = ref()

const typeOptions = [
  { label: '目录', value: 1 },
  { label: '菜单', value: 2 },
  { label: '按钮', value: 3 },
  { label: '接口', value: 4 },
]

const rules = {
  permCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  permName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择权限类型', trigger: 'change' }],
}

const cascaderOptions = computed(() => [
  { id: 0, permName: '根节点', children: list.value },
])

async function fetchData() {
  loading.value = true
  try {
    const res = await permissionApi.permissionTree()
    if (res.code === 200 && res.data) {
      list.value = res.data
    }
  } finally {
    loading.value = false
  }
}

function openCreate() {
  dialogTitle.value = '新增权限'
  form.id = undefined
  form.permCode = ''
  form.permName = ''
  form.type = 2
  form.parentId = 0
  form.path = ''
  form.sort = 0
  form.status = 1
  dialogVisible.value = true
}

function openEdit(row: PermissionVO) {
  dialogTitle.value = '编辑权限'
  form.id = row.id
  form.permCode = row.permCode
  form.permName = row.permName
  form.type = row.type
  form.parentId = row.parentId || 0
  form.path = row.path || ''
  form.sort = row.sort
  form.status = row.status
  dialogVisible.value = true
}

async function save() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  const payload: PermissionDTO = {
    permCode: form.permCode,
    permName: form.permName,
    type: form.type,
    parentId: form.parentId,
    path: form.path,
    sort: form.sort,
    status: form.status,
  }
  const res = form.id ? await permissionApi.updatePermission(form.id, payload) : await permissionApi.createPermission(payload)
  if (res.code === 200) {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } else {
    ElMessage.error(res.message || '保存失败')
  }
}

async function remove(row: PermissionVO) {
  try {
    await ElMessageBox.confirm('确认删除该权限吗？', '提示', { type: 'warning' })
    const res = await permissionApi.deletePermission(row.id)
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

async function toggleStatus(row: PermissionVO) {
  const status = row.status === 1 ? 0 : 1
  const res = await permissionApi.updatePermissionStatus(row.id, status)
  if (res.code === 200) {
    ElMessage.success('状态更新成功')
    fetchData()
  } else {
    ElMessage.error(res.message || '状态更新失败')
  }
}

function typeLabel(type: number) {
  return typeOptions.find((o) => o.value === type)?.label || type
}

onMounted(fetchData)
</script>

<template>
  <div class="space-y-4">
    <el-card>
      <div class="flex justify-between items-center">
        <span class="text-gray-700 font-medium">权限树</span>
        <el-button v-if="hasPermission('permission:create')" type="success" @click="openCreate">新增</el-button>
      </div>
    </el-card>

    <el-card>
      <el-table
        v-loading="loading"
        :data="list"
        row-key="id"
        :tree-props="{ children: 'children' }"
        default-expand-all
        border
        stripe
      >
        <el-table-column prop="permName" label="权限名称" min-width="180" />
        <el-table-column prop="permCode" label="权限编码" min-width="180" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ typeLabel(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" min-width="180" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="hasPermission('permission:update')" type="primary" size="small" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="hasPermission('permission:update')" size="small" @click="toggleStatus(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
            <el-button v-if="hasPermission('permission:delete')" type="danger" size="small" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="权限编码" prop="permCode">
          <el-input v-model="form.permCode" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="权限名称" prop="permName">
          <el-input v-model="form.permName" />
        </el-form-item>
        <el-form-item label="权限类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
            <el-option v-for="opt in typeOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="父级权限">
          <el-cascader
            v-model="form.parentId"
            :options="cascaderOptions"
            :props="{ value: 'id', label: 'permName', children: 'children', checkStrictly: true, emitPath: false }"
            placeholder="根节点"
            style="width: 100%"
            clearable
          />
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" placeholder="路由或接口地址" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" style="width: 100%" />
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
  </div>
</template>
