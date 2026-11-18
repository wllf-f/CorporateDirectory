<template>
  <el-header class="header">
    <div class="header-content">
      <h1 class="title-link" @click="goHome">{{ props.title }}</h1>
      <div class="user-info">
        <span>欢迎，{{ userStore.userInfo?.empName }} ({{ userStore.userInfo?.empRole }})</span>
        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
      </div>
    </div>
  </el-header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'

interface Props {
  title?: string
  homePath?: string // 首页路径
}

const props = withDefaults(defineProps<Props>(), {
  title: '企业通讯录管理系统 - 员工',
  homePath: '/employee/dashboard'
})

const router = useRouter()
const userStore = useUserStore()

const goHome = () => {
  router.push(props.homePath)
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    userStore.clearUserInfo()
    router.push('/login')
  } catch {
    // 用户取消操作
  }
}
</script>

<style scoped>
.header {
  background-color: #e6a23c;
  color: white;
  padding: 0;
  height: 60px;
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-content h1 {
  margin: 0;
  font-size: 18px;
}

.title-link {
  cursor: pointer;
  transition: color 0.3s ease;
}

.title-link:hover {
  color: rgba(255, 255, 255, 0.8) !important;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}
</style>
