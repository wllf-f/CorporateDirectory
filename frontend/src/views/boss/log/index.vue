<template>
  <el-main class="main-content">
    <div class="page-header">
      <h1>操作日志</h1>
    </div>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>日志列表</span>
        </div>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="operDate" label="操作时间" min-width="180">
          <template #default="scope">
            {{ formatDate(scope.row.operDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="loginId" label="登录ID" width="100" />
        <el-table-column prop="operatorId" label="操作员工工号" min-width="120" />
        <el-table-column prop="operatedTable" label="操作表" min-width="120">
          <template #default="scope">
            <el-tag :type="getTableType(scope.row.operatedTable)">
              {{ getTableName(scope.row.operatedTable) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operation" label="操作类型" width="100">
          <template #default="scope">
            <el-tag :type="getOperationType(scope.row.operation)">
              {{ scope.row.operation }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tableId" label="表ID" width="100" />
        <el-table-column label="操作详情" min-width="400">
          <template #default="scope">
            <div class="operation-detail">
              <div v-if="scope.row.oldData" class="data-item">
                <span class="label">旧数据：</span>
                <span class="value">{{ formatData(scope.row.oldData) }}</span>
              </div>
              <div v-if="scope.row.newData" class="data-item">
                <span class="label">新数据：</span>
                <span class="value">{{ formatData(scope.row.newData) }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </el-main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getOperLogList,
  type OperLog,
  type QueryDto
} from '@/api/log'

// 表格数据
const tableData = ref<OperLog[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取操作日志列表
const fetchOperLogList = async () => {
  loading.value = true
  try {
    const params: QueryDto = {
      pageNumber: currentPage.value,
      pageSize: pageSize.value
    }

    const response = await getOperLogList(params)

    if (response.code === 200) {
      tableData.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error(response.message || '获取操作日志失败')
    }
  } catch (error) {
    console.error('获取操作日志失败:', error)
    ElMessage.error('获取操作日志失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN') + ' ' +
         new Date(dateString).toLocaleTimeString('zh-CN')
}

// 获取操作类型标签样式
const getOperationType = (operation: string) => {
  switch (operation) {
    case 'INSERT': return 'success'
    case 'UPDATE': return 'warning'
    case 'DELETE': return 'danger'
    default: return ''
  }
}

// 获取表类型标签样式
const getTableType = (tableName: string) => {
  switch (tableName) {
    case 'employee': return 'primary'
    case 'customer': return 'success'
    case 'department': return 'info'
    default: return ''
  }
}

// 获取表名称
const getTableName = (tableName: string) => {
  switch (tableName) {
    case 'employee': return '员工表'
    case 'customer': return '客户表'
    case 'department': return '部门表'
    default: return tableName
  }
}

// 格式化数据内容
const formatData = (data: string) => {
  try {
    // 尝试解析JSON
    const parsed = JSON.parse(data)
    if (typeof parsed === 'object') {
      return JSON.stringify(parsed, null, 2)
    }
    return data
  } catch {
    // 如果不是JSON，直接返回
    return data
  }
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
  fetchOperLogList()
}

// 当前页改变
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchOperLogList()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchOperLogList()
})
</script>

<style scoped>
.main-content {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  background: white;
  padding: 20px 24px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 操作详情样式 */
.operation-detail {
  max-width: 400px;
}

.data-item {
  margin-bottom: 8px;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 3px solid #667eea;
}

.data-item:last-child {
  margin-bottom: 0;
}

.data-item .label {
  font-weight: 600;
  color: #495057;
  margin-right: 8px;
}

.data-item .value {
  color: #6c757d;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  word-break: break-all;
  white-space: pre-wrap;
}

/* 搜索卡片美化 */
.search-card {
  margin-bottom: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.search-card .el-card__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px 12px 0 0;
  padding: 16px 20px;
}

.search-card .el-card__header .card-header {
  font-weight: 600;
  font-size: 16px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  padding: 20px;
  justify-content: center;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

/* 主表格卡片美化 */
.el-card {
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  overflow: hidden;
}

.el-card .el-card__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px 12px 0 0;
  padding: 16px 20px;
}

.el-card .el-card__header .card-header {
  font-weight: 600;
  font-size: 16px;
}

/* 表格美化 */
.el-table {
  border-radius: 0 0 12px 12px;
}

.el-table th {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  font-weight: 600;
  color: #495057;
  border-bottom: 2px solid #dee2e6;
}

.el-table td {
  border-bottom: 1px solid #f1f3f4;
  transition: all 0.3s ease;
}

.el-table tbody tr:hover {
  background: linear-gradient(135deg, #f8f9ff 0%, #e8f2ff 100%);
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 按钮美化 */
.el-button {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.el-button--primary:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

/* 标签美化 */
.el-tag {
  border-radius: 20px;
  font-weight: 500;
  padding: 4px 12px;
}

.el-tag--success {
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
  border: none;
  color: #2d3748;
}

.el-tag--warning {
  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
  border: none;
  color: #2d3748;
}

.el-tag--danger {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  border: none;
  color: #2d3748;
}

/* 分页美化 */
.pagination-container {
  margin-top: 24px;
  padding: 20px;
  background: white;
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.el-pagination {
  justify-content: center;
}

.el-pagination .el-pager li {
  border-radius: 6px;
  margin: 0 4px;
  transition: all 0.3s ease;
}

.el-pagination .el-pager li:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.el-pagination .el-pager li.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* 数据单元格美化 */
.data-cell {
  display: block;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.5;
  color: #4a5568;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }

  .search-form {
    flex-direction: column;
    align-items: center;
  }

  .el-table {
    font-size: 14px;
  }

  .data-cell {
    max-width: 200px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.el-card {
  animation: fadeInUp 0.6s ease-out;
}

.el-card:nth-child(1) {
  animation-delay: 0.1s;
}

.el-card:nth-child(2) {
  animation-delay: 0.2s;
}
</style>
