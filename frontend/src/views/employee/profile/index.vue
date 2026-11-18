<template>
  <el-main class="main-content">
    <div class="page-header">
      <h1>个人信息管理</h1>
    </div>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
        </div>
      </template>

      <div class="profile-content">
        <el-descriptions :column="2" border class="info-display" size="large">
          <el-descriptions-item label="工号">{{ userStore.userInfo?.empNumber }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ userStore.userInfo?.empName }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ userStore.userInfo?.deptName }}</el-descriptions-item>
          <el-descriptions-item label="角色">{{ userStore.userInfo?.empRole }}</el-descriptions-item>
          <el-descriptions-item label="电话">{{ userStore.userInfo?.empPhone || '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ userStore.userInfo?.empEmail || '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ userStore.userInfo?.isActive }}</el-descriptions-item>
          <el-descriptions-item label="入职时间">{{ formatDate(userStore.userInfo?.empCreateTime) }}</el-descriptions-item>
        </el-descriptions>

        <div class="edit-section">
          <el-button type="primary" @click="showEditDialog = true" style="margin-top: 20px;">
            <el-icon><Edit /></el-icon>
            编辑信息
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 编辑信息对话框 -->
    <el-dialog
      v-model="showEditDialog"
      title="编辑个人信息"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="姓名" prop="empName">
          <el-input v-model="formData.empName" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="电话" prop="empPhone">
          <el-input v-model="formData.empPhone" placeholder="请输入电话号码" />
        </el-form-item>

        <el-form-item label="邮箱" prop="empEmail">
          <el-input v-model="formData.empEmail" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="formData.newPassword"
            type="password"
            placeholder="留空表示不修改密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="formData.confirmPassword"
            type="password"
            placeholder="再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateProfile" :loading="submitLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </el-main>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { updateProfile } from '@/api/employee'

// 用户store
const userStore = useUserStore()

// 对话框显示控制
const showEditDialog = ref(false)
const submitLoading = ref(false)

// 表单引用
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive({
  empName: '',
  empPhone: '',
  empEmail: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const formRules = reactive({
  empName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  empPhone: [
    { required: true, message: '请输入电话号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  empEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  newPassword: [
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value && value.length < 6) {
          callback(new Error('密码长度不能少于6位'))
        } else if (value && formData.confirmPassword && value !== formData.confirmPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: ['blur', 'change']
    }
  ],
  confirmPassword: [
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value && formData.newPassword && value !== formData.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

// 格式化日期
const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 初始化表单数据
const initFormData = () => {
  if (userStore.userInfo) {
    formData.empName = userStore.userInfo.empName
    formData.empPhone = userStore.userInfo.empPhone || ''
    formData.empEmail = userStore.userInfo.empEmail || ''
    formData.newPassword = ''
    formData.confirmPassword = ''
  }
}

// 处理更新个人信息
const handleUpdateProfile = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const response = await updateProfile(
          userStore.userInfo!.empId,
          userStore.userInfo!.empNumber,
          userStore.userInfo!.empNumber, // empNumber保持不变
          formData.empName,
          formData.empPhone,
          formData.empEmail,
          userStore.userInfo!.empRole, // 传入当前用户的角色
          formData.newPassword || undefined
        )

        ElMessage.success('个人信息更新成功')
        showEditDialog.value = false

        // 更新本地用户信息
        userStore.updateUserInfo({
          empName: formData.empName,
          empPhone: formData.empPhone,
          empEmail: formData.empEmail
        })

      } catch (error) {
        console.error('更新个人信息失败:', error)
        ElMessage.error('更新失败，请重试')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 监听对话框打开，初始化表单数据
watch(showEditDialog, (newValue) => {
  if (newValue) {
    initFormData()
  }
})

// 组件挂载时初始化
onMounted(() => {
  console.log('Profile page mounted')
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

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.profile-content {
  padding: 20px 0;
}

.info-display {
  margin-bottom: 20px;
}

.edit-section {
  text-align: center;
}

.dialog-footer {
  text-align: right;
}
</style>