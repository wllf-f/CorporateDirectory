import { API_CONFIG, API_ENDPOINTS, apiRequest } from './config'
import type { UserInfo } from '@/stores/user'

interface LoginRequest {
  empNumber: string
  password: string
}

interface LoginResponse {
  code: number
  message: string
  data: UserInfo
}

// 登录API
export const login = async (data: LoginRequest): Promise<LoginResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.LOGIN}`, {
    method: 'POST',
    body: JSON.stringify(data)
  })
}

// 使用token刷新登录信息
export const refreshLogin = async (): Promise<LoginResponse> => {
  // 直接使用fetch，不通过apiRequest（因为不需要Bearer token header）
  const token = localStorage.getItem('token')
  const userInfoStr = localStorage.getItem('userInfo')

  if (!token || !userInfoStr) {
    throw new Error('No token or user info found')
  }

  const userInfo = JSON.parse(userInfoStr)

  const requestBody = {
    empNumber: userInfo.empNumber,
    token: token  // 直接用token字段
  }

  const response = await fetch(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.LOGIN}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestBody)
  })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  return response.json()
}





