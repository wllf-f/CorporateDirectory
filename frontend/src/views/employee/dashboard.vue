<template>
  <el-main class="main-content">
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-number">{{ userStore.userInfo?.deptCount }}</div>
            <div class="stats-label">部门数量</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-number">{{ departmentEmployeeCount }}</div>
            <div class="stats-label">部门员工总数</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-number">{{ customerCount }}</div>
            <div class="stats-label">客户数量</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-number">{{ userStore.userInfo?.empId }}</div>
            <div class="stats-label">员工ID</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-card class="info-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>
      <div class="user-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="工号">{{ userStore.userInfo?.empNumber }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ userStore.userInfo?.empName }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ userStore.userInfo?.deptName }}</el-descriptions-item>
          <el-descriptions-item label="角色">{{ userStore.userInfo?.empRole }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ userStore.userInfo?.isActive }}</el-descriptions-item>
          <el-descriptions-item label="入职时间">{{ formatDate(userStore.userInfo?.empCreateTime) }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <el-card class="management-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>功能管理</span>
        </div>
      </template>
      <div class="management-grid">
        <el-row :gutter="15">
          <el-col :span="4">
            <div class="management-item" @click="handleSelect('2')">
              <el-icon size="56" class="management-icon"><Management /></el-icon>
              <div class="management-title">公司部门信息</div>
              <div class="management-desc">查看部门结构</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="handleSelect('3')">
              <el-icon size="56" class="management-icon"><User /></el-icon>
              <div class="management-title">公司员工信息</div>
              <div class="management-desc">查看公司员工资料</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="handleSelect('4')">
              <el-icon size="56" class="management-icon"><Avatar /></el-icon>
              <div class="management-title">客户信息管理</div>
              <div class="management-desc">管理客户资料</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="handleSelect('5')">
              <el-icon size="56" class="management-icon"><Setting /></el-icon>
              <div class="management-title">个人信息</div>
              <div class="management-desc">修改个人资料</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </el-main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { Management, User, Avatar, Setting } from '@element-plus/icons-vue'
import { getEmployeeList, type EmployeeResponse } from '@/api/employee'
import { getCustomerList, type CustomerResponse } from '@/api/customer'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const departmentEmployeeCount = ref(0)
const customerCount = ref(0)
const loading = ref(false)

// 获取部门员工总数
const fetchDepartmentEmployeeCount = async () => {
  loading.value = true
  try {
    const response: EmployeeResponse = await getEmployeeList({
      pageNumber: 1,
      pageSize: 1, // 只获取总数
      data: {
        deptName: userStore.userInfo?.deptName // 使用当前登录用户的部门名称作为查询条件
      }
    })

    if (response.code === 200) {
      departmentEmployeeCount.value = response.data.total
    } else {
      console.error('获取部门员工总数失败:', response.message)
    }
  } catch (error) {
    console.error('获取部门员工总数失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取客户数量
const fetchCustomerCount = async () => {
  try {
    const response: CustomerResponse = await getCustomerList({
      pageNumber: 1,
      pageSize: 1, // 只获取总数
      data: {
        empName: userStore.userInfo?.empName // 使用当前登录用户的姓名作为查询条件
      }
    })

    if (response.code === 200) {
      customerCount.value = response.data.total
    } else {
      console.error('获取客户数量失败:', response.message)
    }
  } catch (error) {
    console.error('获取客户数量失败:', error)
  }
}

const handleSelect = (key: string) => {
  switch (key) {
    case '2': // 公司部门信息
      router.push('/employee/department')
      break
    case '3': // 员工管理
      router.push('/employee/employee')
      break
    case '4': // 客户信息管理
      router.push('/employee/customer')
      break
    case '5': // 个人信息
      router.push('/employee/profile')
      break
    default:
      console.log('Unknown menu item:', key)
  }
}

const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 组件挂载时获取数据
onMounted(() => {
  fetchDepartmentEmployeeCount()
  fetchCustomerCount()
})
</script>

<style scoped>
.main-content {
  padding: 20px;
  background-color: #f8f9fa;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  text-align: center;
  height: 140px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.stats-number {
  font-size: 36px;
  font-weight: bold;
  color: #67c23a;
  margin-bottom: 8px;
}

.stats-label {
  color: #666;
  font-size: 14px;
}

.info-card {
  margin-top: 20px;
}

.management-card {
  margin-top: 20px;
}

.card-header {
  font-weight: bold;
}

.user-details {
  padding: 10px 0;
}

.management-grid {
  padding: 20px 0;
}

.management-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #fff;
  height: 140px;
  justify-content: center;
  text-align: center;
}

.management-item:hover {
  border-color: #67c23a;
  box-shadow: 0 2px 12px 0 rgba(103, 194, 58, 0.1);
  transform: translateY(-2px);
}

.management-icon {
  margin-bottom: 8px;
  color: #67c23a;
}

.management-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
  font-size: 14px;
}

.management-desc {
  color: #909399;
  font-size: 12px;
}
</style>
