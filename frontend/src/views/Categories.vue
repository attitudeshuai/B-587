<template>
  <div class="categories-page">
    <div class="page-header">
      <h2 class="page-title">分类管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="openDialog()">
        添加分类
      </el-button>
    </div>

    <div class="card">
      <el-table v-loading="loading" :data="categories" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="icon" label="图标" width="80">
          <template slot-scope="scope">
            <span style="font-size: 24px">{{ scope.row.icon || '📦' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="分类名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
            <el-button type="text" class="danger-text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      :title="isEdit ? '编辑分类' : '添加分类'"
      :visible.sync="dialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="请输入Emoji图标，如：🍎" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入分类描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
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
import { getCategories, createCategory, updateCategory, deleteCategory } from '@/api'

export default {
  name: 'Categories',
  data() {
    return {
      loading: false,
      submitting: false,
      categories: [],
      dialogVisible: false,
      isEdit: false,
      form: this.getInitForm(),
      rules: {
        name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchCategories()
  },
  methods: {
    getInitForm() {
      return {
        name: '',
        icon: '',
        description: '',
        sortOrder: 0
      }
    },
    async fetchCategories() {
      this.loading = true
      try {
        const res = await getCategories()
        this.categories = res.data
      } catch (e) {}
      this.loading = false
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
            await updateCategory(this.form.id, this.form)
            this.$message.success('更新成功')
          } else {
            await createCategory(this.form)
            this.$message.success('创建成功')
          }
          this.dialogVisible = false
          this.fetchCategories()
        } catch (e) {}
        this.submitting = false
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除分类 "${row.name}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteCategory(row.id)
          this.$message.success('删除成功')
          this.fetchCategories()
        } catch (e) {
          // 错误信息已由 API 拦截器显示，此处不需要额外处理
        }
      }).catch(() => {})
    },
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.categories-page {
  max-width: 1200px;
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

.danger-text {
  color: #f56c6c;
}

.danger-text:hover {
  color: #f78989;
}
</style>
