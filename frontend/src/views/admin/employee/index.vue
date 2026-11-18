<template>
  <el-main class="main-content">
    <div class="page-header">
      <h1>员工管理</h1>
      <div class="header-actions">
        <el-switch
          v-model="showPassword"
          active-text="显示密码"
          inactive-text="隐藏密码"
          style="margin-right: 20px;"
        />
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加员工
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input
            v-model="searchForm.empName"
            placeholder="请输入姓名"
            clearable
            style="width: 120px"
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="电话">
          <el-input
            v-model="searchForm.empPhone"
            placeholder="请输入电话"
            clearable
            style="width: 130px"
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="searchForm.empEmail"
            placeholder="请输入邮箱"
            clearable
            style="width: 150px"
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.isActive"
            placeholder="请选择状态"
            clearable
            style="width: 100px"
            @change="handleSearch"
            @clear="handleSearch"
          >
            <el-option label="在职" value="在职" />
            <el-option label="离职" value="离职" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>员工列表</span>
        </div>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="empId" label="员工ID" width="80" />
        <el-table-column prop="empNumber" label="工号" min-width="120" />
        <el-table-column prop="empName" label="姓名" min-width="100" />
        <el-table-column prop="empGender" label="性别" width="80" />
        <el-table-column prop="empPhone" label="电话" min-width="130" />
        <el-table-column prop="empEmail" label="邮箱" min-width="180" />
        <el-table-column prop="deptName" label="部门" min-width="120" />
        <el-table-column prop="empRole" label="角色" width="100" />
        <el-table-column prop="isActive" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.isActive === '在职' ? 'success' : 'danger'">
              {{ scope.row.isActive }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="密码" min-width="120">
          <template #default="scope">
            <span v-if="!showPassword">{{ '*'.repeat(scope.row.password?.length || 6) }}</span>
            <span v-else style="font-family: monospace;">{{ scope.row.password }}</span>
          </template>
        </el-table-column>
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
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <div class="operation-buttons">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="warning" @click="handleResign(scope.row)">离职</el-button>
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号" prop="empNumber">
              <el-input v-model="formData.empNumber" placeholder="请输入工号" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="empName">
              <el-input v-model="formData.empName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="empGender">
              <el-select v-model="formData.empGender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="empPhone">
              <el-input v-model="formData.empPhone" placeholder="请输入电话号码" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="empEmail">
              <el-input v-model="formData.empEmail" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="isEdit">
          <el-col :span="12">
            <el-form-item label="部门" prop="deptId">
              <el-select v-model="formData.deptId" placeholder="请选择部门" style="width: 100%">
                <el-option
                  v-for="dept in departmentList"
                  :key="dept.deptId"
                  :label="dept.deptName"
                  :value="dept.deptId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="isActive">
              <el-select v-model="formData.isActive" placeholder="请选择状态" style="width: 100%">
                <el-option label="在职" value="在职" />
                <el-option label="离职" value="离职" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="密码" prop="password">
          <el-input v-model="formData.password" type="password" :placeholder="isEdit ? '留空表示不修改密码' : '请输入密码'" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import {
  getEmployeeList,
  addEmployee,
  updateEmployee,
  deleteEmployee,
  type Employee
} from '@/api/employee'
import { getDepartmentList, type Department } from '@/api/department'
import { useUserStore } from '@/stores/user'

// 组件挂载状态
const isMounted = ref(false)

// 密码显示控制
const showPassword = ref(false)

// 表格数据
const tableData = ref<Employee[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 部门列表
const departmentList = ref<Department[]>([])

// 用户store
const userStore = useUserStore()

// 搜索表单
const searchForm = reactive({
  empName: '',
  empPhone: '',
  empEmail: '',
  deptName: '', // 默认本部门（在初始化时设置）
  isActive: '在职' as string // 默认在职
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('添加员工')
const submitLoading = ref(false)

// 表单
const formRef = ref<FormInstance>()
const formData = reactive({
  empNumber: '',
  empName: '',
  empGender: '男',
  empPhone: '',
  empEmail: '',
  empRole: '员工',
  password: '',
  deptId: 0,
  isActive: '在职'
})

const isEdit = ref(false)
const editId = ref<number | undefined>()

// 表单验证规则
const formRules = reactive({
  empNumber: [
    { required: true, message: '请输入工号', trigger: 'blur' }
  ],
  empName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  empGender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  empPhone: [
    { required: true, message: '请输入电话号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  empEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  deptId: [
    {
      validator: (rule: any, value: number, callback: Function) => {
        // 编辑模式下才需要验证部门
        if (isEdit.value && (!value || value === 0)) {
          callback(new Error('请选择部门'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  isActive: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  password: [
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (!isEdit.value && (!value || value.trim() === '')) {
          callback(new Error('请输入密码'))
        } else if (value && value.length < 6) {
          callback(new Error('密码长度不能少于6位'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

// 获取员工列表
const fetchEmployeeList = async () => {
  loading.value = true
  try {
    // 构建搜索参数
    const searchData: any = {}
    if (searchForm.empName?.trim()) searchData.empName = searchForm.empName.trim()
    if (searchForm.empPhone?.trim()) searchData.empPhone = searchForm.empPhone.trim()
    if (searchForm.empEmail?.trim()) searchData.empEmail = searchForm.empEmail.trim()
    // 管理员默认搜索本部门
    if (searchForm.deptName?.trim()) {
      searchData.deptName = searchForm.deptName.trim()
    } else if (userStore.userInfo?.deptName) {
      // 如果没有设置部门，默认使用当前用户的部门
      searchData.deptName = userStore.userInfo.deptName
    }
    if (searchForm.isActive) searchData.isActive = searchForm.isActive

    const params = {
      pageNumber: currentPage.value,
      pageSize: pageSize.value,
      ...(Object.keys(searchData).length > 0 && { data: searchData })
    }

    console.log('Search params:', params)

    const response = await getEmployeeList(params)

    if (response.code === 200) {
      tableData.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error(response.message || '获取员工列表失败')
    }
  } catch (error) {
    console.error('获取员工列表失败:', error)
    ElMessage.error('获取员工列表失败')
  } finally {
    loading.value = false
  }
}

// 获取部门列表
const fetchDepartmentList = async () => {
  try {
    const response = await getDepartmentList({
      pageNumber: 1,
      pageSize: 100 // 获取所有部门
    })

    if (response.code === 200) {
      departmentList.value = response.data.records
      // 部门列表已获取，默认选择"所有部门"（已在初始化时设置）
    }
  } catch (error) {
    console.error('获取部门列表失败:', error)
  }
}

// 获取当前用户的部门ID
const getCurrentDeptId = (): number => {
  if (!userStore.userInfo?.deptName) return 0
  const currentDept = departmentList.value.find(dept => dept.deptName === userStore.userInfo?.deptName)
  return currentDept?.deptId || 0
}

// 处理添加
const handleAdd = () => {
  dialogTitle.value = '添加员工'
  isEdit.value = false
  editId.value = undefined
  formData.empNumber = ''
  formData.empName = ''
  formData.empGender = '男'
  formData.empPhone = ''
  formData.empEmail = ''
  formData.empRole = '员工' // 默认角色为员工
  formData.password = ''
  // 默认使用当前用户的部门
  formData.deptId = getCurrentDeptId()
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row: Employee) => {
  dialogTitle.value = '编辑员工'
  isEdit.value = true
  editId.value = row.empId
  formData.empNumber = row.empNumber
  formData.empName = row.empName
  formData.empGender = row.empGender
  formData.empPhone = row.empPhone
  formData.empEmail = row.empEmail
  formData.empRole = '员工' // 编辑时也默认设置为员工
  formData.isActive = row.isActive || '在职'
  formData.deptId = row.deptId
  formData.password = '' // 编辑时密码字段为空
  dialogVisible.value = true
}

// 处理离职
const handleResign = async (row: Employee) => {
  try {
    await ElMessageBox.confirm(
      `确定要将员工"${row.empName}"标记为离职状态吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteEmployee(row.empId!, userStore.userInfo?.empNumber || '')

    if (response.code === 200) {
      ElMessage.success('离职处理成功')
      fetchEmployeeList()
    } else {
      ElMessage.error(response.message || '离职处理失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('离职处理失败:', error)
      ElMessage.error('离职处理失败')
    }
  }
}


// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        // 构建提交数据
        const submitData = { ...formData }

        // 如果是编辑模式且密码为空，则不传递密码字段
        if (isEdit.value && (!submitData.password || submitData.password.trim() === '')) {
          // 编辑时若密码为空，则不提交 password 字段
          (submitData as any).password = undefined
        }

        let response
        if (isEdit.value) {
          // 编辑模式下不传递empRole字段，避免管理员编辑自己时角色被改为员工
          response = await updateEmployee(
            editId.value!,
            userStore.userInfo?.empNumber || '',
            formData.empNumber,
            formData.empName,
            formData.empGender,
            formData.empPhone,
            formData.empEmail,
            formData.deptId,
            undefined as any, // 编辑时不传递empRole
            formData.password || undefined,
            formData.isActive || '在职'
          )
        } else {
          response = await addEmployee(
            userStore.userInfo?.empNumber || '',
            formData.empNumber,
            formData.empName,
            formData.empGender,
            formData.empPhone,
            formData.empEmail,
            formData.deptId,
            formData.empRole, // 添加时传递empRole
            formData.password
          )
        }

        if (response.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
          dialogVisible.value = false
          fetchEmployeeList()
        } else {
          ElMessage.error(response.message || (isEdit.value ? '修改失败' : '添加失败'))
        }
      } catch (error) {
        console.error(isEdit.value ? '修改员工失败:' : '添加员工失败:', error)
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
  fetchEmployeeList()
}

// 当前页改变
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchEmployeeList()
}

// 格式化日期
const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN') + ' ' +
         new Date(dateString).toLocaleTimeString('zh-CN')
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1 // 搜索时重置到第一页
  fetchEmployeeList()
}

// 处理重置
const handleReset = () => {
  searchForm.empName = ''
  searchForm.empPhone = ''
  searchForm.empEmail = ''
  searchForm.deptName = userStore.userInfo?.deptName || '' // 重置为本部门
  searchForm.isActive = '在职' // 重置为默认值
  currentPage.value = 1
  fetchEmployeeList()
}

// 组件挂载时获取数据
onMounted(() => {
  console.log('Employee page mounted')
  isMounted.value = true
  // 初始化搜索表单，默认使用本部门
  if (userStore.userInfo?.deptName) {
    searchForm.deptName = userStore.userInfo.deptName
  }
  fetchDepartmentList().then(() => {
    fetchEmployeeList()
  })
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
  align-items: center;
}

.header-actions .el-switch {
  margin-right: 8px;
}

/* 搜索卡片美化 */
.search-card {
  margin-bottom: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.search-card .el-card__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px 12px 0 0;
  padding: 16px 20px;
}

.search-card .el-card__header .card-header {
  font-weight: 600;
  font-size: 16px;
}

.search-form {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  padding: 20px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

/* 主表格卡片美化 */
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

/* 标签美化 */
.el-tag {
  border-radius: 20px;
  font-weight: 500;
  padding: 4px 12px;
}

.el-tag--success {
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
  border: none;
  color: #2d3748;
}

.el-tag--danger {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  border: none;
  color: #2d3748;
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

.search-form {
    grid-template-columns: 1fr;
    gap: 12px;
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

.el-card:nth-child(1) {
  animation-delay: 0.1s;
}

.el-card:nth-child(2) {
  animation-delay: 0.2s;
}

/* 密码显示切换动画 */
.el-switch {
  transition: all 0.3s ease;
}

.el-switch.is-checked {
  opacity: 0.8;
}
</style>
