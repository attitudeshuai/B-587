<template>
  <div class="login-container">
    <div class="login-bg"></div>
    <div class="login-card">
      <div class="login-header">
        <div class="logo">🍎</div>
        <h1>水果批发仓库管理系统</h1>
        <p>Fruit Wholesale Warehouse Management</p>
      </div>
      <el-form ref="loginForm" :model="form" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            prefix-icon="el-icon-user"
            placeholder="请输入用户名"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            prefix-icon="el-icon-lock"
            placeholder="请输入密码"
            size="large"
            show-password
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <p>测试账号: admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api'

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const res = await login(this.form)
          localStorage.setItem('user', JSON.stringify(res.data))
          this.$message.success('登录成功')
          this.$router.push('/')
        } catch (e) {
          // 错误已在拦截器处理
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    radial-gradient(circle at 20% 80%, rgba(255,255,255,0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255,255,255,0.15) 0%, transparent 50%);
}

.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 48px 40px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.2);
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  font-size: 64px;
  margin-bottom: 16px;
}

.login-header h1 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 8px;
  font-weight: 600;
}

.login-header p {
  font-size: 14px;
  color: #909399;
}

.login-form {
  margin-top: 24px;
}

.login-form .el-input__inner {
  height: 48px;
  border-radius: 8px;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  margin-top: 8px;
}

.login-btn:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4190 100%);
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #ebeef5;
}

.login-footer p {
  font-size: 13px;
  color: #909399;
}

@media (max-width: 480px) {
  .login-card {
    width: 90%;
    padding: 32px 24px;
  }
}
</style>
