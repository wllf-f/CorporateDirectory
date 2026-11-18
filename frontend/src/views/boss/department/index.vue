<template>
  <el-main class="main-content">
    <div class="page-header">
      <h1>部门管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加部门
        </el-button>
      </div>
    </div>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门列表</span>
        </div>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="deptId" label="部门ID" width="80" />
        <el-table-column prop="deptName" label="部门名称" min-width="120" />
        <el-table-column prop="managerName" label="负责人" min-width="120" />
        <el-table-column prop="managerTel" label="负责人电话" min-width="150" />
        <el-table-column label="创建时间" min-width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="更新时间" min-width="180">
          <template #default="scope">
            {{ formatDate(scope.row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <div class="operation-buttons">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="formData.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="负责人" prop="managerId">
          <el-select
            v-model="formData.managerId"
            placeholder="请选择负责人"
            style="width: 100%"
          >
            <el-option
              v-for="emp in employeeList"
              :key="emp.empId"
              :label="`${emp.empName} (${emp.empNumber})`"
              :value="emp.empId"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </el-main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import {
  getDepartmentList,
  addDepartment,
  updateDepartment,
  deleteDepartment,
  type Department
} from '@/api/department'
import { getEmployeeList, type Employee } from '@/api/employee'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 表格数据
const tableData = ref<Department[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 员工列表
const employeeList = ref<Employee[]>([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('添加部门')
const submitLoading = ref(false)
const dialogKey = ref(0) // 用于强制重新渲染select

// 表单
const formRef = ref<FormInstance>()
const formData = reactive({
  deptName: '',
  managerId: undefined as number | undefined
})

const isEdit = ref(false)
const editId = ref<number | undefined>()
const originalFormData = reactive({
  deptName: '',
  managerId: undefined as number | undefined
})

// 表单验证规则
const formRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  managerId: [
    { required: true, message: '请选择负责人', trigger: 'change' },
    {
      validator: (rule: any, value: any, callback: any) => {
        if (value === undefined || value === null || value === '') {
          callback(new Error('请选择负责人'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

// 获取部门列表
const fetchDepartmentList = async () => {
  loading.value = true
  try {
    const response = await getDepartmentList({
      pageNumber: currentPage.value,
      pageSize: pageSize.value
    })

    if (response.code === 200) {
      tableData.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error(response.message || '获取部门列表失败')
    }
  } catch (error) {
    console.error('获取部门列表失败:', error)
    ElMessage.error('获取部门列表失败')
  } finally {
    loading.value = false
  }
}

// 获取员工列表
const fetchEmployeeList = async () => {
  try {
    const response = await getEmployeeList({
      pageNumber: 1,
      pageSize: 1000 // 获取所有员工
    })

    if (response.code === 200) {
      employeeList.value = response.data.records
    }
  } catch (error) {
    console.error('获取员工列表失败:', error)
  }
}

// 处理添加
const handleAdd = () => {
  dialogTitle.value = '添加部门'
  isEdit.value = false
  editId.value = undefined
  formData.deptName = ''
  formData.managerId = undefined
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row: Department) => {
  dialogTitle.value = '编辑部门'
  isEdit.value = true
  editId.value = row.deptId

  // 保存原始数据
  originalFormData.deptName = row.deptName
  originalFormData.managerId = row.managerId

  // 设置表单数据
  formData.deptName = row.deptName
  formData.managerId = row.managerId

  console.log('编辑部门 - managerId:', row.managerId, '类型:', typeof row.managerId)

  dialogVisible.value = true
}

// 处理删除
const handleDelete = async (row: Department) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除部门"${row.deptName}"吗？此操作不可恢复！`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }
    )

    const response = await deleteDepartment(
      row.deptId!,
      userStore.userInfo?.empNumber || ''
    )
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchDepartmentList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除部门失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 检测表单变化
const getChangedFields = () => {
  const changes: any = {
    changeBy: userStore.userInfo?.empNumber || ''
  }

  // 只发送实际修改的字段
  if (formData.deptName !== originalFormData.deptName) {
    changes.deptName = formData.deptName
  }

  if (formData.managerId !== originalFormData.managerId) {
    changes.managerId = formData.managerId
  }

  console.log('原始数据:', originalFormData)
  console.log('当前数据:', formData)
  console.log('变化数据:', changes)

  return changes
}

// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (isEdit.value) {
          const changes = getChangedFields()
          // 如果没有任何字段发生变化，给出提示
          if (Object.keys(changes).length <= 1) { // 只包含changeBy
            ElMessage.warning('请至少修改一个字段')
            return
          }
          response = await updateDepartment(editId.value!, changes)
        } else {
          response = await addDepartment(
            formData.deptName,
            formData.managerId!,
            userStore.userInfo?.empNumber || ''
          )
        }

        if (response.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
          dialogVisible.value = false
          fetchDepartmentList()
        } else {
          ElMessage.error(response.message || (isEdit.value ? '修改失败' : '添加失败'))
        }
      } catch (error) {
        console.error(isEdit.value ? '修改部门失败:' : '添加部门失败:', error)
        ElMessage.error(isEdit.value ? '修改失败' : '添加失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
  fetchDepartmentList()
}

// 当前页改变
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchDepartmentList()
}


// 格式化日期
const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN') + ' ' +
         new Date(dateString).toLocaleTimeString('zh-CN')
}

// 监听对话框打开，确保负责人正确选中
watch(() => dialogVisible.value, (newVal) => {
  if (newVal && isEdit.value) {
    // 确保负责人正确设置
    if (originalFormData.managerId) {
      formData.managerId = originalFormData.managerId
    }
  }
})

// 组件挂载时获取数据
onMounted(() => {
  fetchDepartmentList()
  fetchEmployeeList()
})
</script>

<style scoped>
.main-content {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  background: white;
  padding: 20px 24px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* 卡片美化 */
.el-card {
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  overflow: hidden;
}

.el-card .el-card__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px 12px 0 0;
  padding: 16px 20px;
}

.el-card .el-card__header .card-header {
  font-weight: 600;
  font-size: 16px;
}

/* 表格美化 */
.el-table {
  border-radius: 0 0 12px 12px;
}

.el-table th {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  font-weight: 600;
  color: #495057;
  border-bottom: 2px solid #dee2e6;
}

.el-table td {
  border-bottom: 1px solid #f1f3f4;
  transition: all 0.3s ease;
}

.el-table tbody tr:hover {
  background: linear-gradient(135deg, #f8f9ff 0%, #e8f2ff 100%);
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 按钮美化 */
.el-button {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.el-button--primary:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

.el-button--warning {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  border: none;
  color: white;
}

.el-button--warning:hover {
  background: linear-gradient(135deg, #e86bf0 0%, #e64c60 100%);
}

.el-button--danger {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
  border: none;
}

.el-button--danger:hover {
  background: linear-gradient(135deg, #ff5a5a 0%, #e64a42 100%);
}

/* 分页美化 */
.pagination-container {
  margin-top: 24px;
  padding: 20px;
  background: white;
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.el-pagination {
  justify-content: center;
}

.el-pagination .el-pager li {
  border-radius: 6px;
  margin: 0 4px;
  transition: all 0.3s ease;
}

.el-pagination .el-pager li:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.el-pagination .el-pager li.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* 对话框美化 */
.el-dialog {
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.el-dialog .el-dialog__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  margin: 0;
  padding: 20px 24px;
  border-radius: 16px 16px 0 0;
}

.el-dialog .el-dialog__title {
  font-weight: 600;
  font-size: 18px;
}

.el-dialog .el-dialog__body {
  padding: 24px;
}

.el-dialog .el-form-item__label {
  font-weight: 500;
  color: #4a5568;
}

.el-dialog .el-input__inner,
.el-dialog .el-select .el-input__inner {
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.el-dialog .el-input__inner:focus,
.el-dialog .el-select .el-input__inner:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.dialog-footer {
  text-align: right;
  padding: 16px 24px 24px;
  background: #f8f9fa;
  border-radius: 0 0 16px 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }

  .header-actions {
    justify-content: center;
    flex-wrap: wrap;
  }

  .el-table {
    font-size: 14px;
  }
}

/* 操作按钮样式 */
.operation-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
}

.operation-buttons .el-button {
  padding: 4px 8px;
  min-width: auto;
  font-size: 12px;
  height: 28px;
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.el-card {
  animation: fadeInUp 0.6s ease-out;
}
</style>
