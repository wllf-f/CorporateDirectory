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
        <span>公司部门信息</span>
      </el-menu-item>
      <el-menu-item index="employee">
        <el-icon><UserFilled /></el-icon>
        <span>公司员工信息</span>
      </el-menu-item>
      <el-menu-item index="customer">
        <el-icon><Avatar /></el-icon>
        <span>客户信息管理</span>
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
import { House, Management, Avatar, Setting, UserFilled } from '@element-plus/icons-vue'

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
    case 'dashboard': // 首页
      router.push('/employee/dashboard')
      break
    case 'department': // 公司部门信息
      router.push('/employee/department')
      break
    case 'employee': // 员工管理
      router.push('/employee/employee')
      break
    case 'customer': // 客户信息管理
      router.push('/employee/customer')
      break
    case 'profile': // 个人信息
      router.push('/employee/profile')
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
