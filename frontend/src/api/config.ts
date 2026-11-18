// API配置
export const API_CONFIG = {
  BASE_URL: 'http://localhost:8082/wei',
  TIMEOUT: 10000
}

// API接口路径
export const API_ENDPOINTS = {
  LOGIN: '/login',
  DEPARTMENT_LIST: '/department/list',
  DEPARTMENT_ADD: '/department/add',
  DEPARTMENT_UPDATE: '/department/update',
  DEPARTMENT_DELETE: '/department/delete',
  DEPARTMENT_REMOVE: '/department/remove',
  EMPLOYEE_LIST: '/employee/list',
  EMPLOYEE_ADD: '/employee/add',
  EMPLOYEE_UPDATE: '/employee/update',
  EMPLOYEE_DELETE: '/employee/delete',
  EMPLOYEE_REMOVE: '/employee/remove',
  CUSTOMER_LIST: '/customer/list',
  CUSTOMER_ADD: '/customer/add',
  CUSTOMER_UPDATE: '/customer/update',
  CUSTOMER_DELETE: '/customer/delete',
  CUSTOMER_REMOVE: '/customer/remove',
  OPERLOG_LIST: '/operLog/list'
}

// 通用请求函数，自动添加token
export const apiRequest = async (url: string, options: RequestInit = {}): Promise<any> => {
  // 从localStorage获取token
  const token = localStorage.getItem('token')

  // 设置默认请求头
  const defaultHeaders: Record<string, string> = {
    'Content-Type': 'application/json'
  }

  // 如果有token，添加到请求头
  if (token) {
    defaultHeaders['Authorization'] = `Bearer ${token}` // 使用标准的Bearer token格式
  }

  // 合并请求头
  const headers = {
    ...defaultHeaders,
    ...options.headers
  }

  const response = await fetch(url, {
    ...options,
    headers
  })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  return response.json()
}
