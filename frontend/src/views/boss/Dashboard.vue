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
            <div class="stats-number">{{ userStore.userInfo?.employeeCount }}</div>
            <div class="stats-label">员工总数</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-number">{{ userStore.userInfo?.custCount }}</div>
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
          <span>管理功能</span>
        </div>
      </template>
      <div class="management-grid">
        <el-row :gutter="15">
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/boss/department')">
              <el-icon size="48" class="management-icon"><Management /></el-icon>
              <div class="management-title">部门管理</div>
              <div class="management-desc">管理公司部门信息</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/boss/employee')">
              <el-icon size="48" class="management-icon"><User /></el-icon>
              <div class="management-title">员工管理</div>
              <div class="management-desc">管理员工信息和权限</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="$router.push('/boss/customer')">
              <el-icon size="48" class="management-icon"><Avatar /></el-icon>
              <div class="management-title">客户管理</div>
              <div class="management-desc">管理客户信息</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="handleManagementClick('log')">
              <el-icon size="48" class="management-icon"><Document /></el-icon>
              <div class="management-title">日志查询</div>
              <div class="management-desc">查看系统操作日志</div>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="management-item" @click="handleManagementClick('profile')">
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
import { User, Management, Setting, Avatar, Document, UserFilled } from '@element-plus/icons-vue'
import { watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 监听用户信息变化，确保界面更新
watch(() => userStore.userInfo, () => {
  // 数据更新时界面会自动重新渲染
}, { deep: true })

const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const handleManagementClick = (type: string) => {
  switch (type) {
    case 'log':
      // 跳转到日志查询页面
      router.push('/boss/log')
      break
    case 'profile':
      // 跳转到个人信息页面
      router.push('/boss/profile')
      break
    default:
      console.log('Unknown management type:', type)
  }
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
  color: #409eff;
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
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.1);
  transform: translateY(-2px);
}

.management-icon {
  color: #409eff;
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
