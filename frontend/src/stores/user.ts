import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface UserInfo {
  empCreateTime: string
  deptName: string
  empId: number
  empNumber: string
  empName: string
  empPhone?: string
  empEmail?: string
  empRole: '超级管理员' | '管理员' | '员工'
  deptCount: number
  isActive: string
  employeeCount: number
  token: string
  custCount: number
}

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)
  const isLoggedIn = ref(false)

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    isLoggedIn.value = true
    // 保存到localStorage
    localStorage.setItem('userInfo', JSON.stringify(info))
    localStorage.setItem('token', info.token)
  }

  const updateUserInfo = (info: Partial<UserInfo>) => {
    if (userInfo.value) {
      userInfo.value = { ...userInfo.value, ...info }
      // 更新localStorage
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    }
  }

  const clearUserInfo = () => {
    userInfo.value = null
    isLoggedIn.value = false
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
  }

  const loadUserFromStorage = () => {
    const storedUser = localStorage.getItem('userInfo')
    const storedToken = localStorage.getItem('token')
    if (storedUser && storedToken) {
      userInfo.value = JSON.parse(storedUser)
      isLoggedIn.value = true
    }
  }

  // 初始化时从localStorage加载用户信息
  loadUserFromStorage()

  return {
    userInfo,
    isLoggedIn,
    setUserInfo,
    updateUserInfo,
    clearUserInfo,
    loadUserFromStorage
  }
})





