import { API_CONFIG, API_ENDPOINTS, apiRequest } from './config'

export interface Employee {
  empId?: number
  empNumber: string
  empName: string
  empGender: string
  empPhone: string
  empEmail: string
  empRole: string
  isActive: string
  password?: string
  deptId: number
  deptName?: string
  createTime?: string
  updateTime?: string
}

export interface QueryDto {
  pageNumber: number
  pageSize: number
  data?: {
    empName?: string
    empPhone?: string
    empEmail?: string
    deptName?: string
    isActive?: string
  }
}

export interface EmployeeResponse {
  code: number
  message: string
  data: {
    records: Employee[]
    total: number
    size: number
    current: number
    pages: number
  }
}

export interface SimpleResponse {
  code: number
  message: string
}

// 分页查询员工列表
export const getEmployeeList = async (params: QueryDto): Promise<EmployeeResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.EMPLOYEE_LIST}`, {
    method: 'POST',
    body: JSON.stringify(params)
  })
}

// 添加员工
export const addEmployee = async (
  changeBy: string,
  empNumber: string,
  empName: string,
  empGender: string,
  empPhone: string,
  empEmail: string,
  deptId: number,
  empRole: string,
  password: string
): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.EMPLOYEE_ADD}`, {
    method: 'POST',
    body: JSON.stringify({
      changeBy,
      empNumber,
      empName,
      empGender,
      empPhone,
      empEmail,
      deptId,
      empRole,
      password,
      isActive: '在职' // 默认在职
    })
  })
}

// 更新员工
export const updateEmployee = async (
  empId: number,
  changeBy: string,
  empNumber: string,
  empName: string,
  empGender: string,
  empPhone: string,
  empEmail: string,
  deptId: number,
  empRole: string | undefined,
  password: string | undefined,
  isActive: string
): Promise<SimpleResponse> => {
  const requestBody: any = {
    empId,
    changeBy,
    empNumber,
    empName,
    empGender,
    empPhone,
    empEmail,
    deptId,
    isActive
  }

  // 只有当empRole不为undefined时才包含在请求体中
  if (empRole !== undefined) {
    requestBody.empRole = empRole
  }

  // 如果密码不为空，则包含密码字段
  if (password && password.trim() !== '') {
    requestBody.password = password
  }

  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.EMPLOYEE_UPDATE}`, {
    method: 'PUT',
    body: JSON.stringify(requestBody)
  })
}

// 员工离职（标记为离职状态）
export const deleteEmployee = async (empId: number, changeBy: string): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.EMPLOYEE_DELETE}?empId=${empId}&changeBy=${encodeURIComponent(changeBy)}`, {
    method: 'DELETE'
  })
}

// 彻底删除员工
export const removeEmployee = async (empId: number, changeBy: string): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.EMPLOYEE_REMOVE}?empId=${empId}&changeBy=${encodeURIComponent(changeBy)}`, {
    method: 'DELETE'
  })
}

// 更新个人信息
export const updateProfile = async (
  empId: number,
  changeBy: string,
  empNumber: string,
  empName: string,
  empPhone: string,
  empEmail: string,
  empRole: string, // 当前用户的角色
  password?: string
): Promise<SimpleResponse> => {
  const requestBody: any = {
    empId,
    changeBy,
    empNumber,
    empName,
    empPhone,
    empEmail,
    empRole, // 使用传入的当前用户角色
    isActive: '在职' // 个人信息修改时保持在职状态
  }

  // 如果提供了密码，则包含密码字段
  if (password && password.trim()) {
    requestBody.password = password
  }

  // 注意：这里没有包含empGender和deptId，因为个人信息页面不修改这些字段
  // 如果需要修改性别和部门，应该在员工管理页面进行

  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.EMPLOYEE_UPDATE}`, {
    method: 'PUT',
    body: JSON.stringify(requestBody)
  })
}
