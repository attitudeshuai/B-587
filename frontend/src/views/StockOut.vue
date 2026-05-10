<template>
  <div class="stock-out-page">
    <div class="page-header">
      <h2 class="page-title">出库管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="openDialog()">
        新增出库
      </el-button>
    </div>

    <div class="card">
      <el-table v-loading="loading" :data="records" style="width: 100%">
        <el-table-column prop="recordNo" label="出库单号" min-width="160" />
        <el-table-column label="水果" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.fruit ? scope.row.fruit.name : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="客户" min-width="140">
          <template slot-scope="scope">
            {{ scope.row.customer ? scope.row.customer.name : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="数量" width="100">
          <template slot-scope="scope">
            {{ scope.row.quantity }} {{ scope.row.fruit ? scope.row.fruit.unit : '' }}
          </template>
        </el-table-column>
        <el-table-column label="单价" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.unitPrice }}
          </template>
        </el-table-column>
        <el-table-column label="总金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ scope.row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作人" width="100">
          <template slot-scope="scope">
            {{ scope.row.operator ? scope.row.operator.realName : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="出库时间" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-if="total > 0"
        :current-page.sync="page"
        :page-size.sync="size"
        :total="total"
        layout="total, sizes, prev, pager, next"
        :page-sizes="[10, 20, 50]"
        @size-change="fetchRecords"
        @current-change="fetchRecords"
        style="margin-top: 20px; text-align: right"
      />
    </div>

    <el-dialog
      title="新增出库"
      :visible.sync="dialogVisible"
      width="550px"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择水果" prop="fruitId">
          <el-select v-model="form.fruitId" placeholder="请选择水果" filterable style="width: 100%" @change="onFruitChange">
            <el-option
              v-for="fruit in fruits"
              :key="fruit.id"
              :label="`${fruit.name} (库存: ${fruit.stockQuantity}${fruit.unit})`"
              :value="fruit.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="客户">
          <el-select v-model="form.customerId" placeholder="请选择客户" clearable style="width: 100%">
            <el-option
              v-for="cus in customers"
              :key="cus.id"
              :label="`${cus.name}${cus.level === 'VIP' ? ' (VIP)' : ''}`"
              :value="cus.id"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出库数量" prop="quantity">
              <el-input-number v-model="form.quantity" :min="0.01" :max="maxQuantity" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批发单价" prop="unitPrice">
              <el-input-number v-model="form.unitPrice" :min="0.01" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="总金额">
          <span class="total-amount">¥ {{ (form.quantity * form.unitPrice).toFixed(2) }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确认出库</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStockOutRecords, stockOut, getAllFruits, getCustomers } from '@/api'

export default {
  name: 'StockOut',
  data() {
    return {
      loading: false,
      submitting: false,
      records: [],
      fruits: [],
      customers: [],
      total: 0,
      page: 1,
      size: 10,
      dialogVisible: false,
      maxQuantity: 99999,
      form: this.getInitForm(),
      rules: {
        fruitId: [{ required: true, message: '请选择水果', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入出库数量', trigger: 'blur' }],
        unitPrice: [{ required: true, message: '请输入批发单价', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchRecords()
    this.fetchFruits()
    this.fetchCustomers()
  },
  methods: {
    getInitForm() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      return {
        fruitId: null,
        customerId: null,
        quantity: 1,
        unitPrice: 0,
        operatorId: user.id,
        remark: ''
      }
    },
    async fetchRecords() {
      this.loading = true
      try {
        const res = await getStockOutRecords({ page: this.page - 1, size: this.size })
        this.records = res.data.content
        this.total = res.data.totalElements
      } catch (e) {}
      this.loading = false
    },
    async fetchFruits() {
      try {
        const res = await getAllFruits()
        this.fruits = res.data
      } catch (e) {}
    },
    async fetchCustomers() {
      try {
        const res = await getCustomers()
        this.customers = res.data
      } catch (e) {}
    },
    onFruitChange(fruitId) {
      const fruit = this.fruits.find(f => f.id === fruitId)
      if (fruit) {
        this.maxQuantity = fruit.stockQuantity
        this.form.unitPrice = fruit.salePrice
      }
    },
    openDialog() {
      this.form = this.getInitForm()
      this.maxQuantity = 99999
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          await stockOut(this.form)
          this.$message.success('出库成功')
          this.dialogVisible = false
          this.fetchRecords()
          this.fetchFruits()
        } catch (e) {
          const errorCode = e.errorCode || ''
          if (errorCode === 'INSUFFICIENT_STOCK') {
            this.$message.warning('库存不足，请减少出库数量后重试')
          } else if (errorCode === 'FRUIT_NOT_FOUND') {
            this.$message.error('所选水果不存在，请重新选择')
          } else if (e.businessError) {
            this.$message.error(e.friendlyMessage || '出库失败')
          }
        }
        this.submitting = false
      })
    },
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.stock-out-page {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.amount {
  color: #f56c6c;
  font-weight: 600;
}

.total-amount {
  font-size: 24px;
  font-weight: 700;
  color: #f56c6c;
}
</style>
