<template>
  <el-aside width="200px" class="sidebar">
    <el-menu
      :default-active="activeIndex"
      class="sidebar-menu"
      @select="handleSelect"
    >
      <el-menu-item index="dashboard">
        <el-icon><House /></el-icon>
        <span>首页</span>
      </el-menu-item>
      <el-menu-item index="department">
        <el-icon><Management /></el-icon>
        <span>部门管理</span>
      </el-menu-item>
      <el-menu-item index="employee">
        <el-icon><User /></el-icon>
        <span>员工管理</span>
      </el-menu-item>
      <el-menu-item index="customer">
        <el-icon><Avatar /></el-icon>
        <span>客户管理</span>
      </el-menu-item>
      <el-menu-item index="log">
        <el-icon><Document /></el-icon>
        <span>日志查询</span>
      </el-menu-item>
      <el-menu-item index="profile">
        <el-icon><Setting /></el-icon>
        <span>个人信息</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { House, Management, User, Avatar, Setting, Document } from '@element-plus/icons-vue'

interface Props {
  activeIndex?: string
}

const props = withDefaults(defineProps<Props>(), {
  activeIndex: 'dashboard'
})

const emit = defineEmits<{
  select: [index: string]
}>()

const router = useRouter()

const handleSelect = (index: string) => {
  emit('select', index)

  switch (index) {
    case 'dashboard':
      router.push('/boss')
      break
    case 'department':
      router.push('/boss/department')
      break
    case 'company-employee':
      router.push('/boss/company-employee')
      break
    case 'employee':
      router.push('/boss/employee')
      break
    case 'customer':
      router.push('/boss/customer')
      break
    case 'log':
      router.push('/boss/log')
      break
    case 'profile':
      router.push('/boss/profile')
      break
    default:
      console.log('Unknown menu item:', index)
  }
}
</script>

<style scoped>
.sidebar {
  background-color: #f5f5f5;
  border-right: 1px solid #e6e6e6;
}

.sidebar-menu {
  border-right: none;
}
</style>
