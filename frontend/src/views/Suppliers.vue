<template>
  <div class="suppliers-page">
    <div class="page-header">
      <h2 class="page-title">供应商管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="openDialog()">
        添加供应商
      </el-button>
    </div>

    <div class="card">
      <el-table v-loading="loading" :data="suppliers" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="供应商名称" min-width="150" />
        <el-table-column prop="contactName" label="联系人" width="100" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
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
    </div>

    <el-dialog
      :title="isEdit ? '编辑供应商' : '添加供应商'"
      :visible.sync="dialogVisible"
      width="550px"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="供应商名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人">
              <el-input v-model="form.contactName" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
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
import { getSuppliers, createSupplier, updateSupplier, deleteSupplier } from '@/api'

export default {
  name: 'Suppliers',
  data() {
    return {
      loading: false,
      submitting: false,
      suppliers: [],
      dialogVisible: false,
      isEdit: false,
      form: this.getInitForm(),
      rules: {
        name: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchSuppliers()
  },
  methods: {
    getInitForm() {
      return {
        name: '',
        contactName: '',
        phone: '',
        address: '',
        remark: '',
        status: 1
      }
    },
    async fetchSuppliers() {
      this.loading = true
      try {
        const res = await getSuppliers()
        this.suppliers = res.data
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
            await updateSupplier(this.form.id, this.form)
            this.$message.success('更新成功')
          } else {
            await createSupplier(this.form)
            this.$message.success('创建成功')
          }
          this.dialogVisible = false
          this.fetchSuppliers()
        } catch (e) {}
        this.submitting = false
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除供应商 "${row.name}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteSupplier(row.id)
          this.$message.success('删除成功')
          this.fetchSuppliers()
        } catch (e) {
          // 错误信息已由 API 拦截器显示，此处不需要额外处理
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.suppliers-page {
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

.danger-text {
  color: #f56c6c;
}

.danger-text:hover {
  color: #f78989;
}
</style>
