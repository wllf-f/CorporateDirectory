<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="login-header">
          <h2>企业通讯录管理系统</h2>
        </div>
      </template>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-width="80px"
        class="login-form"
      >
        <el-form-item label="工号" prop="empNumber">
          <el-input
            v-model="loginForm.empNumber"
            placeholder="请输入工号"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/auth'
import type { FormInstance } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  empNumber: '',
  password: ''
})

const loginRules = {
  empNumber: [
    { required: true, message: '请输入工号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
  } catch (error) {
    return
  }

  loading.value = true

  try {
    const response = await login({
      empNumber: loginForm.empNumber,
      password: loginForm.password
    })

    if (response.code === 200) {
      // 保存用户信息
      userStore.setUserInfo(response.data)

      ElMessage.success('登录成功')

      // 根据角色跳转到对应页面
      switch (response.data.empRole) {
        case '超级管理员':
          router.push('/boss')
          break
        case '管理员':
          router.push('/admin')
          break
        case '员工':
          router.push('/employee')
          break
        default:
          ElMessage.error('未知用户角色')
      }
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  color: #333;
}

.login-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.login-form {
  padding: 20px 0;
}

.login-btn {
  width: 100%;
}
</style>













