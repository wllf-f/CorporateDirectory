import { API_CONFIG, API_ENDPOINTS, apiRequest } from './config'

export interface Department {
  deptId?: number
  deptName: string
  managerId?: number
  managerName?: string
  managerTel?: string
  createTime?: string
  updateTime?: string
}

export interface QueryDto {
  pageNumber: number
  pageSize: number
}

export interface DepartmentResponse {
  code: number
  message: string
  data: {
    records: Department[]
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

// 分页查询部门列表
export const getDepartmentList = async (params: QueryDto): Promise<DepartmentResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.DEPARTMENT_LIST}`, {
    method: 'POST',
    body: JSON.stringify(params)
  })
}

// 添加部门
export const addDepartment = async (deptName: string, managerId: number, changeBy: string): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.DEPARTMENT_ADD}`, {
    method: 'POST',
    body: JSON.stringify({
      changeBy,
      deptName,
      managerId
    })
  })
}

// 更新部门
export const updateDepartment = async (deptId: number, changes: any): Promise<SimpleResponse> => {
  const requestBody = {
    deptId,
    ...changes
  }

  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.DEPARTMENT_UPDATE}`, {
    method: 'PUT',
    body: JSON.stringify(requestBody)
  })
}

// 删除部门
export const deleteDepartment = async (deptId: number, changeBy: string): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.DEPARTMENT_REMOVE}?deptId=${deptId}&changeBy=${encodeURIComponent(changeBy)}`, {
    method: 'DELETE'
  })
}
