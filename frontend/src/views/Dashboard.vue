<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="12" :sm="6">
        <div class="stat-card">
          <div class="stat-icon">🍎</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalFruits }}</div>
            <div class="stat-label">水果种类</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card green">
          <div class="stat-icon">📦</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.todayStockIn }}</div>
            <div class="stat-label">今日入库</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card orange">
          <div class="stat-icon">🚚</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.todayStockOut }}</div>
            <div class="stat-label">今日出库</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card blue">
          <div class="stat-icon">👥</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalCustomers }}</div>
            <div class="stat-label">客户数量</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 金额统计 -->
    <el-row :gutter="20" class="amount-row">
      <el-col :xs="24" :sm="12">
        <div class="card amount-card">
          <div class="amount-header">
            <span class="amount-title">今日入库金额</span>
            <i class="el-icon-download amount-icon green"></i>
          </div>
          <div class="amount-value green">¥ {{ formatMoney(stats.todayStockInAmount) }}</div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12">
        <div class="card amount-card">
          <div class="amount-header">
            <span class="amount-title">今日出库金额</span>
            <i class="el-icon-upload2 amount-icon orange"></i>
          </div>
          <div class="amount-value orange">¥ {{ formatMoney(stats.todayStockOutAmount) }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 低库存预警 -->
      <el-col :xs="24" :md="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-warning-outline"></i> 库存预警
            </span>
            <el-tag type="danger" size="small">{{ lowStockFruits.length }} 项</el-tag>
          </div>
          <div v-if="lowStockFruits.length === 0" class="empty-state">
            <i class="el-icon-check"></i>
            <p>库存充足，暂无预警</p>
          </div>
          <el-table v-else :data="lowStockFruits" style="width: 100%" size="small">
            <el-table-column prop="name" label="水果名称" />
            <el-table-column prop="stockQuantity" label="当前库存">
              <template slot-scope="scope">
                <span class="stock-warning">{{ scope.row.stockQuantity }} {{ scope.row.unit }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="minStock" label="最低库存">
              <template slot-scope="scope">
                {{ scope.row.minStock }} {{ scope.row.unit }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <!-- 分类统计 -->
      <el-col :xs="24" :md="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-pie-chart"></i> 分类统计
            </span>
          </div>
          <div class="category-list">
            <div
              v-for="item in stats.categoryStocks"
              :key="item.categoryName"
              class="category-item"
            >
              <div class="category-info">
                <span class="category-name">{{ item.categoryName }}</span>
                <span class="category-count">{{ item.fruitCount }} 种</span>
              </div>
              <el-progress
                :percentage="getPercentage(item.fruitCount)"
                :show-text="false"
                :stroke-width="8"
                color="#667eea"
              />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <div class="card quick-actions">
      <div class="card-header">
        <span class="card-title">
          <i class="el-icon-s-operation"></i> 快捷操作
        </span>
      </div>
      <el-row :gutter="16">
        <el-col :xs="12" :sm="6">
          <div class="action-btn" @click="$router.push('/stock-in')">
            <i class="el-icon-download"></i>
            <span>新增入库</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6">
          <div class="action-btn" @click="$router.push('/stock-out')">
            <i class="el-icon-upload2"></i>
            <span>新增出库</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6">
          <div class="action-btn" @click="$router.push('/fruits')">
            <i class="el-icon-plus"></i>
            <span>添加水果</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6">
          <div class="action-btn" @click="$router.push('/customers')">
            <i class="el-icon-user"></i>
            <span>客户管理</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getDashboardStats } from '@/api'

export default {
  name: 'Dashboard',
  data() {
    return {
      loading: false,
      stats: {
        totalFruits: 0,
        totalCategories: 0,
        totalSuppliers: 0,
        totalCustomers: 0,
        todayStockIn: 0,
        todayStockOut: 0,
        todayStockInAmount: 0,
        todayStockOutAmount: 0,
        lowStockFruits: [],
        categoryStocks: []
      }
    }
  },
  computed: {
    lowStockFruits() {
      return this.stats.lowStockFruits || []
    }
  },
  created() {
    this.fetchStats()
  },
  methods: {
    async fetchStats() {
      this.loading = true
      try {
        const res = await getDashboardStats()
        this.stats = res.data
      } catch (e) {
        // 错误已处理
      } finally {
        this.loading = false
      }
    },
    formatMoney(value) {
      if (!value) return '0.00'
      return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
    getPercentage(count) {
      const max = Math.max(...(this.stats.categoryStocks || []).map(c => c.fruitCount), 1)
      return Math.round((count / max) * 100)
    }
  }
}
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(102, 126, 234, 0.4);
}

.stat-card.green {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.stat-card.green:hover {
  box-shadow: 0 12px 24px rgba(17, 153, 142, 0.4);
}

.stat-card.orange {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-card.orange:hover {
  box-shadow: 0 12px 24px rgba(245, 87, 108, 0.4);
}

.stat-card.blue {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-card.blue:hover {
  box-shadow: 0 12px 24px rgba(79, 172, 254, 0.4);
}

.stat-icon {
  font-size: 40px;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.amount-row {
  margin-bottom: 20px;
}

.amount-card {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.amount-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.amount-title {
  font-size: 14px;
  color: #909399;
}

.amount-icon {
  font-size: 24px;
}

.amount-icon.green {
  color: #11998e;
}

.amount-icon.orange {
  color: #f5576c;
}

.amount-value {
  font-size: 28px;
  font-weight: 700;
}

.amount-value.green {
  color: #11998e;
}

.amount-value.orange {
  color: #f5576c;
}

.card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-title i {
  color: #667eea;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.empty-state i {
  font-size: 48px;
  color: #67c23a;
  margin-bottom: 12px;
}

.stock-warning {
  color: #f56c6c;
  font-weight: 600;
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.category-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.category-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.category-name {
  font-size: 14px;
  color: #606266;
}

.category-count {
  font-size: 14px;
  color: #909399;
}

.quick-actions .el-row {
  margin-top: 8px;
}

.action-btn {
  background: #f5f7fa;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.action-btn:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  transform: translateY(-2px);
}

.action-btn i {
  font-size: 28px;
}

.action-btn span {
  font-size: 14px;
}

@media (max-width: 768px) {
  .stat-card {
    padding: 16px;
    margin-bottom: 12px;
  }
  
  .stat-icon {
    font-size: 32px;
  }
  
  .stat-value {
    font-size: 24px;
  }
}
</style>
