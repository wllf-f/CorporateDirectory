<template>
  <el-main class="main-content">
    <div class="page-header">
      <h1>客户管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加客户
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input
            v-model="searchForm.custName"
            placeholder="请输入姓名"
            clearable
            style="width: 120px"
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="手机">
          <el-input
            v-model="searchForm.custPhone"
            placeholder="请输入手机"
            clearable
            style="width: 130px"
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="searchForm.custEmail"
            placeholder="请输入邮箱"
            clearable
            style="width: 150px"
            @clear="handleSearch"
          />
        </el-form-item>
        <el-form-item label="员工姓名">
          <el-select
            v-model="searchForm.empName"
            placeholder="请选择员工"
            clearable
            style="width: 120px"
            @change="handleSearch"
            @clear="handleSearch"
          >
            <el-option
              v-for="emp in empListFromApi"
              :key="emp"
              :label="emp"
              :value="emp"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否删除">
          <el-select
            v-model="searchForm.isDeleted"
            placeholder="请选择状态"
            clearable
            style="width: 100px"
            @change="handleSearch"
            @clear="handleSearch"
          >
            <el-option label="否" value="否" />
            <el-option label="是" value="是" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-select
            v-model="searchForm.deptName"
            placeholder="请选择部门"
            clearable
            style="width: 120px"
            @change="handleSearch"
            @clear="handleSearch"
          >
            <el-option label="所有部门" value="所有部门" />
            <el-option
              v-for="dept in departmentList"
              :key="dept.deptId"
              :label="dept.deptName"
              :value="dept.deptName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="隐私等级">
          <el-select
            v-model="searchForm.privacyLevel"
            placeholder="请选择等级"
            clearable
            style="width: 110px"
            @change="handleSearch"
            @clear="handleSearch"
          >
            <el-option label="personal" value="personal" />
            <el-option label="group" value="group" />
          </el-select>
        </el-form-item>
        <el-form-item label="分组">
          <el-select
            v-model="searchForm.personalGroup"
            placeholder="全部分组"
            clearable
            style="width: 110px"
            @change="handleSearch"
            @clear="handleSearch"
          >
            <el-option
              v-for="group in groupList"
              :key="group"
              :label="group"
              :value="group"
            />
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
          <span>客户列表</span>
        </div>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="custId" label="客户ID" width="80" />
        <el-table-column prop="custName" label="姓名" min-width="100" />
        <el-table-column prop="custGender" label="性别" width="80" />
        <el-table-column prop="custPhone" label="电话" min-width="130" />
        <el-table-column prop="custEmail" label="邮箱" min-width="180" />
        <el-table-column prop="empName" label="添加员工" min-width="100" />
        <el-table-column prop="deptName" label="负责部门" min-width="120" />
        <el-table-column prop="isDeleted" label="是否删除" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isDeleted === '是' ? 'danger' : 'success'">
              {{ scope.row.isDeleted }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="privacyLevel" label="隐私等级" width="100">
          <template #default="scope">
            <el-tag :type="getPrivacyLevelType(scope.row.privacyLevel)">
              {{ scope.row.privacyLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="personalGroup" label="分组" min-width="120" />
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
              <el-button size="mini" type="warning" @click="handleDelete(scope.row)">删除</el-button>
              <el-button size="mini" type="danger" @click="handleRemove(scope.row)">彻底删除</el-button>
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
            <el-form-item label="姓名" prop="custName">
              <el-input v-model="formData.custName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="custGender">
              <el-select v-model="formData.custGender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电话" prop="custPhone">
              <el-input v-model="formData.custPhone" placeholder="请输入电话号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="custEmail">
              <el-input v-model="formData.custEmail" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
        <el-form-item label="隐私等级" prop="privacyLevel">
          <el-select v-model="formData.privacyLevel" placeholder="请选择等级" style="width: 100%">
            <el-option label="personal" value="personal" />
            <el-option label="group" value="group" />
          </el-select>
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="isEdit" label="是否删除" prop="isDeleted">
              <el-select v-model="formData.isDeleted" placeholder="请选择状态" style="width: 100%">
                <el-option label="否" value="否" />
                <el-option label="是" value="是" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>


        <el-form-item label="负责员工" prop="empName">
          <el-select v-model="formData.empName" placeholder="请选择负责员工" style="width: 100%" @change="handleEmpChange">
            <el-option
              v-for="emp in employeeList"
              :key="emp.empId"
              :label="`${emp.empName} (${emp.deptName})`"
              :value="emp.empName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分组" prop="personalGroup">
          <el-select
            v-model="formData.personalGroup"
            placeholder="请选择分组"
            style="width: 100%"
            @change="handleGroupChange"
          >
            <el-option label="无" value="无" />
            <el-option
              v-for="group in groupList"
              :key="group"
              :label="group"
              :value="group"
            />
            <el-option label="添加新分组" value="__add_new__" />
          </el-select>
        </el-form-item>

        <el-form-item v-if="showNewGroupInput" label="新分组名称" prop="newGroupName">
          <el-input
            v-model="formData.newGroupName"
            placeholder="请输入新分组名称"
            style="width: 100%"
          />
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
import { ref, reactive, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import {
  getCustomerList,
  addCustomer,
  updateCustomer,
  deleteCustomer,
  removeCustomer,
  type Customer
} from '@/api/customer'
import { getDepartmentList, type Department } from '@/api/department'
import { getEmployeeList, type Employee } from '@/api/employee'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 表格数据
const tableData = ref<Customer[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 部门和员工列表
const departmentList = ref<Department[]>([])
const employeeList = ref<Employee[]>([])

// 从API获取的员工列表
const empListFromApi = ref<string[]>([])

// 分组列表
const groupList = ref<string[]>([])

// 新分组输入相关
const showNewGroupInput = ref(false)

// 搜索表单
const searchForm = reactive({
  custName: '',
  custPhone: '',
  custEmail: '',
  empName: '',
  isDeleted: '',
  deptName: '所有部门',
  privacyLevel: '',
  personalGroup: ''
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('添加客户')
const submitLoading = ref(false)

// 表单
const formRef = ref<FormInstance>()
const formData = reactive<Customer & { newGroupName?: string }>({
  custName: '',
  custGender: '男',
  custPhone: '',
  custEmail: '',
  empName: '',
  isDeleted: '否',
  privacyLevel: 'personal',
  personalGroup: '无',
  newGroupName: '',
  addedBy: 0,
  deptName: ''
})

// 选中的员工工号（用于API调用）
const selectedEmpNumber = ref('')

const isEdit = ref(false)
const editId = ref<number | undefined>()

// 表单验证规则
const formRules = {
  custName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  custGender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  custPhone: [
    { required: true, message: '请输入电话号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  custEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  empName: [
    { required: true, message: '请选择负责员工', trigger: 'change' }
  ],
  isDeleted: [
    {
      required: true,
      message: '请选择状态',
      trigger: 'change',
      validator: (rule: any, value: string, callback: Function) => {
        // 只在编辑模式下验证
        if (isEdit.value && (!value || value.trim() === '')) {
          callback(new Error('请选择状态'))
        } else {
          callback()
        }
      }
    }
  ],
  privacyLevel: [
    { required: true, message: '请选择隐私等级', trigger: 'change' }
  ],
  newGroupName: [
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (showNewGroupInput.value && (!value || value.trim() === '')) {
          callback(new Error('请输入新分组名称'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取客户列表
const fetchCustomerList = async () => {
  loading.value = true
  try {
    // 构建搜索参数
    const searchData: any = {}

    // 只有当有实际值时才添加搜索条件，避免传递空字符串给后端
    console.log('Raw search form:', searchForm)

    const custName = searchForm.custName?.trim()
    if (custName && custName.length > 0) {
      searchData.custName = custName
      console.log('Adding custName:', custName)
    }

    const custPhone = searchForm.custPhone?.trim()
    if (custPhone && custPhone.length > 0) {
      searchData.custPhone = custPhone
      console.log('Adding custPhone:', custPhone)
    }

    const custEmail = searchForm.custEmail?.trim()
    if (custEmail && custEmail.length > 0) {
      searchData.custEmail = custEmail
      console.log('Adding custEmail:', custEmail)
    }

    const empName = searchForm.empName?.trim()
    if (empName && empName.length > 0) {
      searchData.empName = empName
      console.log('Adding empName:', empName)
    }

    const isDeleted = searchForm.isDeleted?.trim()
    if (isDeleted && isDeleted.length > 0) {
      searchData.isDeleted = isDeleted
    }

    // 只有当不是"所有部门"时才传递部门名称参数
    const deptName = searchForm.deptName?.trim()
    if (deptName && deptName !== '所有部门') {
      searchData.deptName = deptName
      console.log('Adding deptName:', deptName)
    }

    const privacyLevel = searchForm.privacyLevel?.trim()
    if (privacyLevel && privacyLevel.length > 0) {
      searchData.privacyLevel = privacyLevel
      console.log('Adding privacyLevel:', privacyLevel)
    }

    const personalGroup = searchForm.personalGroup?.trim()
    if (personalGroup && personalGroup.length > 0) {
      searchData.personalGroup = personalGroup
      console.log('Adding personalGroup:', personalGroup)
    }

    let params: any = {
      pageNumber: currentPage.value,
      pageSize: pageSize.value
    }

    // 只有当有搜索条件时才添加data对象
    if (Object.keys(searchData).length > 0) {
      params.data = searchData
      console.log('Final params with data:', JSON.stringify(params))
    } else {
      console.log('Final params without data:', JSON.stringify(params))
    }

    console.log('Customer search params:', params)
    console.log('Search data keys:', Object.keys(searchData))
    console.log('Search data values:', searchData)

    const response = await getCustomerList(params)

    if (response.code === 200) {
      tableData.value = response.data.records
      total.value = response.data.total
      // 保存extraInfo中的empList
      if ((response.data as any).extraInfo?.empList) {
        empListFromApi.value = (response.data as any).extraInfo.empList
      }
      // 保存extraInfo中的personalGroup列表
      if ((response.data as any).extraInfo?.personalGroup) {
        groupList.value = (response.data as any).extraInfo.personalGroup
      }
    } else {
      ElMessage.error(response.message || '获取客户列表失败')
    }
  } catch (error) {
    console.error('获取客户列表失败:', error)
    ElMessage.error('获取客户列表失败')
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
    }
  } catch (error) {
    console.error('获取部门列表失败:', error)
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
  dialogTitle.value = '添加客户'
  isEdit.value = false
  editId.value = undefined
  formData.custName = ''
  formData.custGender = '男'
  formData.custPhone = ''
  formData.custEmail = ''
  formData.empName = userStore.userInfo?.empName || ''
  formData.isDeleted = '否'
  formData.privacyLevel = 'personal'
  formData.personalGroup = '无'
  formData.newGroupName = ''
  formData.addedBy = userStore.userInfo?.empId || 0
  showNewGroupInput.value = false
  selectedEmpNumber.value = userStore.userInfo?.empNumber || ''
  
  // 根据当前用户设置部门名称
  if (userStore.userInfo?.empName) {
    const emp = employeeList.value.find(e => e.empName === userStore.userInfo?.empName)
    if (emp) {
      formData.deptName = emp.deptName
    }
  }
  
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row: Customer) => {
  dialogTitle.value = '编辑客户'
  isEdit.value = true
  editId.value = row.custId
  formData.custName = row.custName
  formData.custGender = row.custGender
  formData.custPhone = row.custPhone
  formData.custEmail = row.custEmail
  formData.empName = row.empName
  formData.isDeleted = row.isDeleted
  formData.privacyLevel = row.privacyLevel
  formData.personalGroup = row.personalGroup
  formData.addedBy = row.addedBy

  // 根据addedBy找到对应的员工信息
  const emp = employeeList.value.find(e => e.empId === row.addedBy)
  if (emp) {
    formData.empName = emp.empName
    formData.deptName = emp.deptName
    selectedEmpNumber.value = emp.empNumber
  } else {
    formData.empName = row.empName
    formData.deptName = row.deptName
    selectedEmpNumber.value = ''
  }

  dialogVisible.value = true
}

// 处理删除
const handleDelete = async (row: Customer) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除客户"${row.custName}"吗？此操作可恢复。`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteCustomer(row.custId!, userStore.userInfo?.empNumber || '')
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchCustomerList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除客户失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 处理彻底删除
const handleRemove = async (row: Customer) => {
  try {
    await ElMessageBox.confirm(
      `确定要彻底删除客户"${row.custName}"吗？此操作不可恢复！`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      }
    )

    const response = await removeCustomer(row.custId!, userStore.userInfo?.empNumber || '')
    if (response.code === 200) {
      ElMessage.success('彻底删除成功')
      fetchCustomerList()
    } else {
      ElMessage.error(response.message || '彻底删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('彻底删除客户失败:', error)
      ElMessage.error('彻底删除失败')
    }
  }
}


// 处理分组变更
const handleGroupChange = (value: string) => {
  if (value === '__add_new__') {
    showNewGroupInput.value = true
    formData.newGroupName = ''
    formData.personalGroup = ''
  } else {
    showNewGroupInput.value = false
    formData.newGroupName = ''
    formData.personalGroup = value
  }
}

// 处理员工变更
const handleEmpChange = (empName: string) => {
  const emp = employeeList.value.find(e => e.empName === empName)
  if (emp) {
    formData.deptName = emp.deptName
    formData.addedBy = emp.empId
    selectedEmpNumber.value = emp.empNumber
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1 // 搜索时重置到第一页
  fetchCustomerList()
}

// 处理重置
const handleReset = () => {
  // 重置所有搜索条件为空或默认值
  Object.assign(searchForm, {
    custName: '',
    custPhone: '',
    custEmail: '',
    empName: '',
    isDeleted: '',
    deptName: '所有部门',
    privacyLevel: '',
    personalGroup: ''
  })
  currentPage.value = 1
  fetchCustomerList()
}

// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        let personalGroup = formData.personalGroup || '无'
        if (showNewGroupInput.value && formData.newGroupName?.trim()) {
          personalGroup = formData.newGroupName.trim()
        }

        if (isEdit.value) {
          // 编辑时，如果选择了有效的负责员工，则传递addedBy，否则不传递
          const responsibleEmpId = formData.addedBy && formData.addedBy > 0 ? formData.addedBy : undefined
          response = await updateCustomer(
            userStore.userInfo?.empNumber || '',
            editId.value!,
            formData.custName,
            formData.custGender,
            formData.custPhone,
            formData.custEmail,
            personalGroup,
            responsibleEmpId, // 只在有有效的负责员工ID时才传递
            formData.privacyLevel,
            formData.isDeleted
          )
        } else {
          response = await addCustomer(
            formData.custName,
            formData.custGender,
            formData.custPhone,
            formData.custEmail,
            personalGroup,
            formData.addedBy || 0,
            formData.privacyLevel
          )
        }

        if (response.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
          dialogVisible.value = false
          fetchCustomerList()
        } else {
          ElMessage.error(response.message || (isEdit.value ? '修改失败' : '添加失败'))
        }
      } catch (error) {
        console.error(isEdit.value ? '修改客户失败:' : '添加客户失败:', error)
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
  fetchCustomerList()
}

// 当前页改变
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchCustomerList()
}

// 获取隐私等级标签类型
const getPrivacyLevelType = (level: string) => {
  switch (level) {
    case 'personal': return 'info'
    case 'group': return 'warning'
    default: return ''
  }
}

// 格式化日期
const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN') + ' ' +
         new Date(dateString).toLocaleTimeString('zh-CN')
}

// 监听对话框关闭，重置新分组相关字段
watch(dialogVisible, (newValue) => {
  if (!newValue) {
    showNewGroupInput.value = false
    formData.newGroupName = ''
  }
})

// 组件挂载时获取数据
onMounted(() => {
  console.log('Customer page mounted')
  fetchCustomerList()
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
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
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

.el-tag--info {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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
</style>
