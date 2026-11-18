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
        <span>{{ routePrefix === '/boss' ? '部门管理' : '公司部门信息' }}</span>
      </el-menu-item>
      <el-menu-item index="company-employee">
        <el-icon><UserFilled /></el-icon>
        <span>公司员工信息</span>
      </el-menu-item>
      <el-menu-item index="employee">
        <el-icon><User /></el-icon>
        <span>员工管理</span>
      </el-menu-item>
      <el-menu-item index="customer">
        <el-icon><Avatar /></el-icon>
        <span>客户管理</span>
      </el-menu-item>
      <el-menu-item v-if="showLog" index="log">
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
import { ElMessage } from 'element-plus'
import { House, User, Management, Setting, Avatar, Document, UserFilled } from '@element-plus/icons-vue'

interface Props {
  activeIndex?: string
  routePrefix?: string // 路由前缀：'/boss' 或 '/admin'
  showLog?: boolean // 是否显示日志查询菜单项
}

const props = withDefaults(defineProps<Props>(), {
  activeIndex: 'dashboard',
  routePrefix: '/boss',
  showLog: true
})

const emit = defineEmits<{
  select: [index: string]
}>()

const router = useRouter()

const handleSelect = (index: string) => {
  emit('select', index)

  const prefix = props.routePrefix

  switch (index) {
    case 'dashboard':
      router.push(prefix)
      break
    case 'department':
      router.push(`${prefix}/department`)
      break
    case 'company-employee':
      router.push(`${prefix}/company-employee`)
      break
    case 'employee':
      router.push(`${prefix}/employee`).catch(err => {
        console.error('Navigation failed:', err)
      })
      break
    case 'customer':
      router.push(`${prefix}/customer`)
      break
    case 'log':
      if (props.showLog) {
        router.push(`${prefix}/log`)
      }
      break
    case 'profile':
      router.push(`${prefix}/profile`)
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
