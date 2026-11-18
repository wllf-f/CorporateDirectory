<template>
  <div class="boss-container">
    <!-- 头部组件 -->
    <Header title="企业通讯录管理系统 - 超级管理员" />

    <el-container class="main-container">
      <!-- 侧边栏组件 -->
      <BossSidebar :active-index="activeIndex" @select="handleSidebarSelect" />

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
import BossSidebar from '@/components/boss/BossSidebar.vue'

const route = useRoute()

// 根据当前路由计算活跃的菜单项
const activeIndex = computed(() => {
  const path = route.path
  if (path === '/boss') return 'dashboard'
  if (path.startsWith('/boss/department')) return 'department'
  if (path.startsWith('/boss/company-employee')) return 'company-employee'
  if (path.startsWith('/boss/employee')) return 'employee'
  if (path.startsWith('/boss/customer')) return 'customer'
  if (path === '/boss/log') return 'log'
  if (path === '/boss/profile') return 'profile'
  return 'dashboard'
})

const handleSidebarSelect = (index: string) => {
  // Sidebar组件已经处理了导航逻辑，这里可以添加额外的处理
  console.log('Sidebar selected:', index)
}
</script>

<style scoped>
.boss-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-container {
  flex: 1;
  height: calc(100vh - 60px);
}
</style>
