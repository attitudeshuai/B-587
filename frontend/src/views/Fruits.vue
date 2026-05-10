<template>
  <div class="fruits-page">
    <div class="page-header">
      <h2 class="page-title">水果管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="openDialog()">
        添加水果
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="card search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="水果名称">
          <el-input v-model="searchForm.name" placeholder="请输入水果名称" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="全部分类" clearable>
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchFruits">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="card">
      <el-table v-loading="loading" :data="fruits" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="水果名称" min-width="120" />
        <el-table-column label="分类" width="100">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.category ? scope.row.category.name : '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="origin" label="产地" width="120" />
        <el-table-column label="进货价" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.purchasePrice }}
          </template>
        </el-table-column>
        <el-table-column label="批发价" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.salePrice }}
          </template>
        </el-table-column>
        <el-table-column label="库存" width="120">
          <template slot-scope="scope">
            <span :class="{ 'stock-warning': scope.row.stockQuantity <= scope.row.minStock }">
              {{ scope.row.stockQuantity }} {{ scope.row.unit }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
            <el-button type="text" class="danger-text" @click="handleDelete(scope.row)">删除</el-button>
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
        @size-change="fetchFruits"
        @current-change="fetchFruits"
        style="margin-top: 20px; text-align: right"
      />
    </div>

    <!-- 表单弹窗 -->
    <el-dialog
      :title="isEdit ? '编辑水果' : '添加水果'"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="水果名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入水果名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select v-model="form.category" placeholder="请选择分类" value-key="id" style="width: 100%">
                <el-option
                  v-for="cat in categories"
                  :key="cat.id"
                  :label="cat.name"
                  :value="cat"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="进货价格" prop="purchasePrice">
              <el-input-number v-model="form.purchasePrice" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批发价格" prop="salePrice">
              <el-input-number v-model="form.salePrice" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="如：斤、个、盒" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最低库存" prop="minStock">
              <el-input-number v-model="form.minStock" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="产地" prop="origin">
          <el-input v-model="form.origin" placeholder="请输入产地" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入水果描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="上架" inactive-text="下架" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFruits, createFruit, updateFruit, deleteFruit, getCategories } from '@/api'

export default {
  name: 'Fruits',
  data() {
    return {
      loading: false,
      submitting: false,
      fruits: [],
      categories: [],
      total: 0,
      page: 1,
      size: 10,
      searchForm: {
        name: '',
        categoryId: null,
        status: null
      },
      dialogVisible: false,
      isEdit: false,
      form: this.getInitForm(),
      rules: {
        name: [{ required: true, message: '请输入水果名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择分类', trigger: 'change' }],
        purchasePrice: [{ required: true, message: '请输入进货价格', trigger: 'blur' }],
        salePrice: [{ required: true, message: '请输入批发价格', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchCategories()
    this.fetchFruits()
  },
  methods: {
    getInitForm() {
      return {
        name: '',
        category: null,
        unit: '斤',
        purchasePrice: 0,
        salePrice: 0,
        stockQuantity: 0,
        minStock: 0,
        origin: '',
        description: '',
        status: 1
      }
    },
    async fetchCategories() {
      try {
        const res = await getCategories()
        this.categories = res.data
      } catch (e) {}
    },
    async fetchFruits() {
      this.loading = true
      try {
        const res = await getFruits({
          page: this.page - 1,
          size: this.size,
          ...this.searchForm
        })
        this.fruits = res.data.content
        this.total = res.data.totalElements
      } catch (e) {}
      this.loading = false
    },
    resetSearch() {
      this.searchForm = { name: '', categoryId: null, status: null }
      this.page = 1
      this.fetchFruits()
    },
    openDialog(row = null) {
      this.isEdit = !!row
      this.form = row ? { ...row } : this.getInitForm()
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
          if (this.isEdit) {
            await updateFruit(this.form.id, this.form)
            this.$message.success('更新成功')
          } else {
            await createFruit(this.form)
            this.$message.success('创建成功')
          }
          this.dialogVisible = false
          this.fetchFruits()
        } catch (e) {}
        this.submitting = false
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除水果 "${row.name}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteFruit(row.id)
          this.$message.success('删除成功')
          this.fetchFruits()
        } catch (e) {
          // 错误信息已由 API 拦截器显示，此处不需要额外处理
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.fruits-page {
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
  margin-bottom: 20px;
}

.search-bar .el-form-item {
  margin-bottom: 0;
}

.stock-warning {
  color: #f56c6c;
  font-weight: 600;
}

.danger-text {
  color: #f56c6c;
}

.danger-text:hover {
  color: #f78989;
}
</style>
