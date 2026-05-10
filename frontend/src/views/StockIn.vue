<template>
  <div class="stock-in-page">
    <div class="page-header">
      <h2 class="page-title">入库管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="openDialog()">
        新增入库
      </el-button>
    </div>

    <div class="card">
      <el-table v-loading="loading" :data="records" style="width: 100%">
        <el-table-column prop="recordNo" label="入库单号" min-width="160" />
        <el-table-column label="水果" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.fruit ? scope.row.fruit.name : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="供应商" min-width="140">
          <template slot-scope="scope">
            {{ scope.row.supplier ? scope.row.supplier.name : '-' }}
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
        <el-table-column label="入库时间" width="180">
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
      title="新增入库"
      :visible.sync="dialogVisible"
      width="550px"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择水果" prop="fruitId">
          <el-select v-model="form.fruitId" placeholder="请选择水果" filterable style="width: 100%">
            <el-option
              v-for="fruit in fruits"
              :key="fruit.id"
              :label="`${fruit.name} (库存: ${fruit.stockQuantity}${fruit.unit})`"
              :value="fruit.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="form.supplierId" placeholder="请选择供应商" clearable style="width: 100%">
            <el-option
              v-for="sup in suppliers"
              :key="sup.id"
              :label="sup.name"
              :value="sup.id"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入库数量" prop="quantity">
              <el-input-number v-model="form.quantity" :min="0.01" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="进货单价" prop="unitPrice">
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
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确认入库</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStockInRecords, stockIn, getAllFruits, getActiveSuppliers } from '@/api'

export default {
  name: 'StockIn',
  data() {
    return {
      loading: false,
      submitting: false,
      records: [],
      fruits: [],
      suppliers: [],
      total: 0,
      page: 1,
      size: 10,
      dialogVisible: false,
      form: this.getInitForm(),
      rules: {
        fruitId: [{ required: true, message: '请选择水果', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入入库数量', trigger: 'blur' }],
        unitPrice: [{ required: true, message: '请输入进货单价', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchRecords()
    this.fetchFruits()
    this.fetchSuppliers()
  },
  methods: {
    getInitForm() {
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      return {
        fruitId: null,
        supplierId: null,
        quantity: 1,
        unitPrice: 0,
        operatorId: user.id,
        remark: ''
      }
    },
    async fetchRecords() {
      this.loading = true
      try {
        const res = await getStockInRecords({ page: this.page - 1, size: this.size })
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
    async fetchSuppliers() {
      try {
        // 只获取启用状态的供应商
        const res = await getActiveSuppliers()
        this.suppliers = res.data
      } catch (e) {}
    },
    openDialog() {
      this.form = this.getInitForm()
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
          await stockIn(this.form)
          this.$message.success('入库成功')
          this.dialogVisible = false
          this.fetchRecords()
          this.fetchFruits()
        } catch (e) {}
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
.stock-in-page {
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
  color: #67c23a;
  font-weight: 600;
}

.total-amount {
  font-size: 24px;
  font-weight: 700;
  color: #67c23a;
}
</style>
