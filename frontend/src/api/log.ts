import { API_CONFIG, API_ENDPOINTS, apiRequest } from './config'

export interface OperLog {
  operDate: string
  loginId: number
  tableId: number
  operatedTable: string
  operatorId: string
  operation: string
  newData?: string
  oldData?: string
}

export interface QueryDto {
  pageNumber: number
  pageSize: number
  data?: {
    operatorId?: string
  }
}

export interface OperLogResponse {
  code: number
  message: string
  data: {
    records: OperLog[]
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

// 查询操作日志列表
export const getOperLogList = async (params: QueryDto): Promise<OperLogResponse> => {
  return apiRequest(`${API_CONFIG.BASE_URL}${API_ENDPOINTS.OPERLOG_LIST}`, {
    method: 'POST',
    body: JSON.stringify(params)
  })
}
