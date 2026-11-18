import { API_CONFIG, API_ENDPOINTS, apiRequest } from './config'

export interface Customer {
  custId?: number
  custName: string
  custGender: string
  custPhone: string
  custEmail: string
  empName?: string
  deptName?: string
  addedBy?: number
  isDeleted: string
  privacyLevel: string
  personalGroup?: string
  createTime?: string
  updateTime?: string
}

export interface QueryDto {
  pageNumber: number
  pageSize: number
  data?: {
    custName?: string
    custPhone?: string
    custEmail?: string
    empName?: string
    isDeleted?: string
    deptName?: string
    privacyLevel?: string
    personalGroup?: string
  }
}

export interface CustomerResponse {
  code: number
  message: string
  data: {
    records: Customer[]
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

// 分页查询客户列表
export const getCustomerList = async (params: QueryDto): Promise<CustomerResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.CUSTOMER_LIST}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(params)
  })

}

// 添加客户
export const addCustomer = async (
  custName: string,
  custGender: string,
  custPhone: string,
  custEmail: string,
  personalGroup: string,
  addedBy: number,
  privacyLevel: string
): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.CUSTOMER_ADD}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      custName,
      custGender,
      custPhone,
      custEmail,
      personalGroup,
      addedBy,
      privacyLevel
    })
  })

}

// 更新客户
export const updateCustomer = async (
  changeBy: string,
  custId: number,
  custName: string,
  custGender: string,
  custPhone: string,
  custEmail: string,
  personalGroup: string,
  addedBy: number | undefined,
  privacyLevel: string,
  isDeleted: string
): Promise<SimpleResponse> => {
  const requestBody: any = {
      changeBy,
      custId,
      custName,
      custGender,
      custPhone,
      custEmail,
      personalGroup,
      privacyLevel,
      isDeleted
  }

  // 只有当addedBy不为undefined时才包含在请求体中，避免传递不存在的员工ID
  if (addedBy !== undefined) {
    requestBody.addedBy = addedBy
  }

  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.CUSTOMER_UPDATE}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestBody)
  })

}

// 删除客户（软删除）
export const deleteCustomer = async (custId: number, changeBy: string): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.CUSTOMER_DELETE}?custId=${custId}&changeBy=${changeBy}`, {
    method: 'DELETE'
  })

}

// 彻底删除客户
export const removeCustomer = async (custId: number, changeBy: string): Promise<SimpleResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.CUSTOMER_REMOVE}?custId=${custId}&changeBy=${changeBy}`, {
    method: 'DELETE'
  })

}

// 获取所有分组列表
export const getGroupList = async (): Promise<{ code: number; message: string; data: string[] }> => {
  // 从客户列表中提取所有不重复的分组
  const response = await getCustomerList({
    pageNumber: 1,
    pageSize: 10000 // 获取足够多的数据来提取所有分组
  })

  if (response.code === 200) {
    const groups = response.data.records
      .map(customer => customer.personalGroup)
      .filter((group): group is string => group !== undefined && group.trim() !== '' && group.trim() !== '无')
      .filter((group, index, arr) => arr.indexOf(group) === index)
      .sort()

    return {
      code: 200,
      message: 'success',
      data: groups
    }
  }

  return {
    code: response.code,
    message: response.message,
    data: []
  }
}
