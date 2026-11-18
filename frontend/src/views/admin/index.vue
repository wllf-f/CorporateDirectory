<template>
  <div class="admin-container">
    <!-- 头部组件 -->
    <Header title="企业通讯录管理系统 - 管理员" :home-path="'/admin'" bg-color="#67c23a" />

    <el-container class="main-container">
      <!-- 侧边栏组件 -->
      <AdminSidebar :active-index="activeIndex" @select="handleSidebarSelect" />

      <!-- 主要内容区域 -->
      <el-container>
        <router-view />
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from '@/components/common/Header.vue'
import AdminSidebar from '@/components/admin/AdminSidebar.vue'

const route = useRoute()

// 根据当前路由计算活跃的菜单项
const activeIndex = computed(() => {
  const path = route.path
  if (path === '/admin') return 'dashboard'
  if (path.startsWith('/admin/department')) return 'department'
  if (path.startsWith('/admin/company-employee')) return 'company-employee'
  if (path.startsWith('/admin/employee')) return 'employee'
  if (path.startsWith('/admin/customer')) return 'customer'
  if (path === '/admin/profile') return 'profile'
  return 'dashboard'
})

const handleSidebarSelect = (index: string) => {
  // Sidebar组件已经处理了导航逻辑，这里可以添加额外的处理
  console.log('Sidebar selected:', index)
}
</script>

<style scoped>
.admin-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-container {
  flex: 1;
  height: calc(100vh - 60px);
}
</style>






