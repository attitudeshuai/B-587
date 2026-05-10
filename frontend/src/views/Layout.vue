<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '240px'" class="layout-aside">
      <div class="logo-container">
        <span class="logo-icon">🍎</span>
        <span v-show="!isCollapse" class="logo-text">水果仓库管理</span>
      </div>
      <el-menu
        :default-active="$route.path"
        :collapse="isCollapse"
        :collapse-transition="false"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
        class="layout-menu"
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页概览</span>
        </el-menu-item>
        <el-menu-item index="/fruits">
          <i class="el-icon-apple"></i>
          <span slot="title">水果管理</span>
        </el-menu-item>
        <el-menu-item index="/categories">
          <i class="el-icon-menu"></i>
          <span slot="title">分类管理</span>
        </el-menu-item>
        <el-menu-item index="/stock-in">
          <i class="el-icon-download"></i>
          <span slot="title">入库管理</span>
        </el-menu-item>
        <el-menu-item index="/stock-out">
          <i class="el-icon-upload2"></i>
          <span slot="title">出库管理</span>
        </el-menu-item>
        <el-menu-item index="/suppliers">
          <i class="el-icon-truck"></i>
          <span slot="title">供应商管理</span>
        </el-menu-item>
        <el-menu-item index="/customers">
          <i class="el-icon-user"></i>
          <span slot="title">客户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="layout-header">
        <div class="header-left">
          <i
            :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"
            class="collapse-btn"
            @click="isCollapse = !isCollapse"
          ></i>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>{{ $route.meta.title || '首页' }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" icon="el-icon-user-solid"></el-avatar>
              <span class="username">{{ user.realName || user.username }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout">
                <i class="el-icon-switch-button"></i> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  data() {
    return {
      isCollapse: false,
      user: {}
    }
  },
  created() {
    const userStr = localStorage.getItem('user')
    if (userStr) {
      this.user = JSON.parse(userStr)
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          localStorage.removeItem('user')
          this.$router.push('/login')
        }).catch(() => {})
      }
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #263445;
  color: #fff;
  overflow: hidden;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  margin-left: 8px;
  white-space: nowrap;
}

.layout-menu {
  border-right: none;
}

.layout-menu:not(.el-menu--collapse) {
  width: 240px;
}

.main-container {
  background-color: #f5f7fa;
}

.layout-header {
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
  transition: color 0.3s;
}

.collapse-btn:hover {
  color: #409EFF;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  color: #606266;
  font-size: 14px;
}

.layout-main {
  padding: 20px;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .layout-aside {
    position: fixed;
    z-index: 100;
    height: 100%;
  }
  
  .username {
    display: none;
  }
}
</style>
