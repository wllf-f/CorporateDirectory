import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/index.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/boss',
      component: () => import('@/views/boss/index.vue'),
      meta: {
        requiresAuth: true,
        roles: ['超级管理员']
      },
      children: [
        {
          path: '',
          name: 'BossDashboard',
          component: () => import('@/views/boss/Dashboard.vue')
        },
        {
          path: 'department',
          name: 'DepartmentManagement',
          component: () => import('@/views/boss/department/index.vue')
        },
        {
          path: 'employee',
          name: 'EmployeeManagement',
          component: () => import('@/views/boss/employee/index.vue')
        },
        {
          path: 'customer',
          name: 'CustomerManagement',
          component: () => import('@/views/boss/customer/index.vue')
        },
        {
          path: 'log',
          name: 'OperLogManagement',
          component: () => import('@/views/boss/log/index.vue'),
          meta: { title: '日志查询' }
        },
        {
          path: 'profile',
          name: 'ProfileManagement',
          component: () => import('@/views/boss/profile/index.vue'),
          meta: { title: '个人信息' }
        }
      ]
    },
    {
      path: '/admin',
      component: () => import('@/views/admin/index.vue'),
      meta: {
        requiresAuth: true,
        roles: ['管理员']
      },
      children: [
        {
          path: '',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/Dashboard.vue')
        },
        {
          path: 'department',
          name: 'AdminDepartmentManagement',
          component: () => import('@/views/admin/department/index.vue')
        },
        {
          path: 'company-employee',
          name: 'AdminCompanyEmployee',
          component: () => import('@/views/admin/company-employee/index.vue')
        },
        {
          path: 'employee',
          name: 'AdminEmployeeManagement',
          component: () => import('@/views/admin/employee/index.vue')
        },
        {
          path: 'customer',
          name: 'AdminCustomerManagement',
          component: () => import('@/views/admin/customer/index.vue')
        },
        {
          path: 'profile',
          name: 'AdminProfileManagement',
          component: () => import('@/views/admin/profile/index.vue'),
          meta: { title: '个人信息' }
        }
      ]
    },
    {
      path: '/employee',
      name: 'Employee',
      component: () => import('@/views/employee/index.vue'),
      meta: {
        requiresAuth: true,
        roles: ['员工']
      },
      children: [
        {
          path: '',
          redirect: '/employee/dashboard'
        },
        {
          path: 'dashboard',
          name: 'EmployeeDashboard',
          component: () => import('@/views/employee/dashboard.vue'),
          meta: {
            requiresAuth: true,
            roles: ['员工']
          }
        },
        {
          path: 'department',
          name: 'EmployeeDepartment',
          component: () => import('@/views/employee/department/index.vue'),
          meta: {
            requiresAuth: true,
            roles: ['员工']
          }
        },
        {
          path: 'employee',
          name: 'EmployeeEmployee',
          component: () => import('@/views/employee/employee/index.vue'),
          meta: {
            requiresAuth: true,
            roles: ['员工']
          }
        },
        {
          path: 'customer',
          name: 'EmployeeCustomer',
          component: () => import('@/views/employee/customer/index.vue'),
          meta: {
            requiresAuth: true,
            roles: ['员工']
          }
        },
        {
          path: 'profile',
          name: 'EmployeeProfile',
          component: () => import('@/views/employee/profile/index.vue'),
          meta: {
            requiresAuth: true,
            roles: ['员工']
          }
        }
      ]
    }
  ],
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()

  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    // 检查token是否存在
    const token = localStorage.getItem('token')
    if (!token) {
      // 没有token，跳转到登录页
      next('/login')
      return
    }

    // 有token时，总是尝试刷新最新数据（包括页面刷新和正常导航）
    try {
      const { refreshLogin } = await import('@/api/auth')
      const response = await refreshLogin()

      if (response.code === 200) {
        userStore.setUserInfo(response.data)
      } else {
        // token无效或过期，清除存储并跳转登录
        userStore.clearUserInfo()
        next('/login')
        return
      }
    } catch (error) {
      console.error('自动登录失败:', error)
      userStore.clearUserInfo()
      next('/login')
      return
    }

    if (!userStore.isLoggedIn) {
      // 未登录，跳转到登录页
      next('/login')
      return
    }

    // 检查角色权限
    if (to.meta.roles && !(to.meta.roles as string[]).includes(userStore.userInfo?.empRole || '')) {
      // 角色不匹配，跳转到对应角色的页面
      const role = userStore.userInfo?.empRole
      switch (role) {
        case '超级管理员':
          next('/boss')
          break
        case '管理员':
          next('/admin')
          break
        case '员工':
          next('/employee')
          break
        default:
          next('/login')
      }
      return
    }
  }

  // 已登录用户访问登录页，重定向到对应角色页面
  if (to.path === '/login' && userStore.isLoggedIn) {
    const role = userStore.userInfo?.empRole
    switch (role) {
      case '超级管理员':
        next('/boss')
        break
      case '管理员':
        next('/admin')
        break
      case '员工':
        next('/employee')
        break
      default:
        next()
    }
    return
  }

  next()
})

export default router
