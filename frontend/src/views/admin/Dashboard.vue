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
            <div class="stats-number">{{ deptEmployeeCount }}</div>
            <div class="stats-label">部门员工总数</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-number">{{ deptCustomerCount }}</div>
            <div class="stats-label">部门客户数量</div>
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
          <span>管理功能</span>
        </div>
      </template>
      <div class="management-grid">
        <el-row :gutter="15">
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/admin/department')">
              <el-icon size="48" class="management-icon"><Management /></el-icon>
              <div class="management-title">公司部门信息</div>
              <div class="management-desc">查看公司部门信息</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/admin/company-employee')">
              <el-icon size="48" class="management-icon"><UserFilled /></el-icon>
              <div class="management-title">公司员工信息</div>
              <div class="management-desc">查看公司全部员工信息</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/admin/employee')">
              <el-icon size="48" class="management-icon"><User /></el-icon>
              <div class="management-title">部门员工管理</div>
              <div class="management-desc">管理员工信息和权限</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/admin/customer')">
              <el-icon size="48" class="management-icon"><Avatar /></el-icon>
              <div class="management-title">客户管理</div>
              <div class="management-desc">管理客户信息</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/admin/profile')">
              <el-icon size="48" class="management-icon"><Setting /></el-icon>
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
import { ref, onMounted, watch } from 'vue'
import { User, Management, Setting, Avatar, UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getEmployeeList } from '@/api/employee'
import { getCustomerList } from '@/api/customer'

const userStore = useUserStore()

// 部门统计数据
const deptEmployeeCount = ref(0)
const deptCustomerCount = ref(0)

// 获取部门员工总数
const fetchDeptEmployeeCount = async () => {
  try {
    if (!userStore.userInfo?.deptName) return
    
    const response = await getEmployeeList({
      pageNumber: 1,
      pageSize: 1, // 只需要总数，不需要实际数据
      data: {
        deptName: userStore.userInfo.deptName,
        isActive: '在职' // 只统计在职员工
      }
    })
    
    if (response.code === 200) {
      deptEmployeeCount.value = response.data.total
    }
  } catch (error) {
    console.error('获取部门员工总数失败:', error)
    // 如果获取失败，使用userStore中的数据作为后备
    deptEmployeeCount.value = userStore.userInfo?.employeeCount || 0
  }
}

// 获取部门客户数量
const fetchDeptCustomerCount = async () => {
  try {
    if (!userStore.userInfo?.deptName) return
    
    const response = await getCustomerList({
      pageNumber: 1,
      pageSize: 1, // 只需要总数，不需要实际数据
      data: {
        deptName: userStore.userInfo.deptName,
        isDeleted: '否' // 只统计未删除的客户
      }
    })
    
    if (response.code === 200) {
      deptCustomerCount.value = response.data.total
    }
  } catch (error) {
    console.error('获取部门客户数量失败:', error)
    // 如果获取失败，使用userStore中的数据作为后备
    deptCustomerCount.value = userStore.userInfo?.custCount || 0
  }
}

// 监听用户信息变化，重新获取统计数据
watch(() => userStore.userInfo, () => {
  if (userStore.userInfo) {
    fetchDeptEmployeeCount()
    fetchDeptCustomerCount()
  }
}, { deep: true, immediate: true })

// 组件挂载时获取数据
onMounted(() => {
  if (userStore.userInfo) {
    fetchDeptEmployeeCount()
    fetchDeptCustomerCount()
  }
})

const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}
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
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.stats-number {
  font-size: 32px;
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

.card-header {
  font-weight: bold;
}

.user-details {
  padding: 10px 0;
}

.management-card {
  margin-top: 20px;
}

.management-grid {
  padding: 20px 0;
}

.management-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #fff;
  height: 120px;
  justify-content: center;
  text-align: center;
}

.management-item:hover {
  border-color: #67c23a;
  box-shadow: 0 2px 12px 0 rgba(103, 194, 58, 0.1);
  transform: translateY(-2px);
}

.management-icon {
  color: #67c23a;
  margin-bottom: 12px;
}

.management-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.management-desc {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}
</style>


