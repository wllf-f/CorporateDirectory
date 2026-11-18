<template>
  <div class="employee-container">
    <!-- 头部组件 -->
    <EmployeeHeader />

    <el-container class="main-container">
      <!-- 侧边栏组件 -->
      <EmployeeSidebar :active-index="activeIndex" @select="handleSidebarSelect" />

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
import EmployeeHeader from '@/components/employee/EmployeeHeader.vue'
import EmployeeSidebar from '@/components/employee/EmployeeSidebar.vue'

const route = useRoute()

// 根据当前路由计算活跃的菜单项
const activeIndex = computed(() => {
  const path = route.path
  if (path === '/employee/dashboard') return 'dashboard'
  if (path.startsWith('/employee/department')) return 'department'
  if (path.startsWith('/employee/employee')) return 'employee'
  if (path.startsWith('/employee/customer')) return 'customer'
  if (path.startsWith('/employee/profile')) return 'profile'
  return 'dashboard'
})

const handleSidebarSelect = (index: string) => {
  console.log('Sidebar selected:', index)
  // Sidebar组件已经处理了导航逻辑，这里可以添加额外的处理
}
</script>

<style scoped>
.employee-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-container {
  flex: 1;
  height: calc(100vh - 60px);
}
</style>






