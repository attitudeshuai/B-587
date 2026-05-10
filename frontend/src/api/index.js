import axios from 'axios'
import { Message } from 'element-ui'

const api = axios.create({
  baseURL: process.env.VUE_APP_API_URL || '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      const err = new Error(res.message || '请求失败')
      err.errorCode = res.errorCode || ''
      err.businessError = true
      return Promise.reject(err)
    }
    return res
  },
  error => {
    let errorMessage = '网络错误'
    let errorCode = ''
    if (error.response) {
      const { data, status } = error.response
      if (data && data.message) {
        errorMessage = data.message
      } else if (status === 400) {
        errorMessage = '请求参数错误'
      } else if (status === 404) {
        errorMessage = '请求的资源不存在'
      } else if (status === 500) {
        errorMessage = '服务器内部错误，请稍后重试'
      }
      if (data && data.errorCode) {
        errorCode = data.errorCode
      }
    } else if (error.message) {
      if (error.message.includes('timeout')) {
        errorMessage = '请求超时，请检查网络连接'
      } else if (error.message.includes('Network Error')) {
        errorMessage = '网络连接失败，请检查网络'
      }
    }
    if (errorCode) {
      error.businessError = true
    } else {
      Message.error(errorMessage)
    }
    error.friendlyMessage = errorMessage
    error.errorCode = errorCode
    return Promise.reject(error)
  }
)

// 认证
export const login = (data) => api.post('/auth/login', data)

// 仪表盘
export const getDashboardStats = () => api.get('/dashboard/stats')

// 分类
export const getCategories = () => api.get('/categories')
export const createCategory = (data) => api.post('/categories', data)
export const updateCategory = (id, data) => api.put(`/categories/${id}`, data)
export const deleteCategory = (id) => api.delete(`/categories/${id}`)

// 水果
export const getFruits = (params) => api.get('/fruits', { params })
export const getAllFruits = () => api.get('/fruits/all')
export const getFruit = (id) => api.get(`/fruits/${id}`)
export const createFruit = (data) => api.post('/fruits', data)
export const updateFruit = (id, data) => api.put(`/fruits/${id}`, data)
export const deleteFruit = (id) => api.delete(`/fruits/${id}`)
export const getLowStockFruits = () => api.get('/fruits/low-stock')

// 供应商
export const getSuppliers = () => api.get('/suppliers')
export const getActiveSuppliers = () => api.get('/suppliers/active')
export const createSupplier = (data) => api.post('/suppliers', data)
export const updateSupplier = (id, data) => api.put(`/suppliers/${id}`, data)
export const deleteSupplier = (id) => api.delete(`/suppliers/${id}`)

// 客户
export const getCustomers = () => api.get('/customers')
export const createCustomer = (data) => api.post('/customers', data)
export const updateCustomer = (id, data) => api.put(`/customers/${id}`, data)
export const deleteCustomer = (id) => api.delete(`/customers/${id}`)

// 库存
export const stockIn = (data) => api.post('/stock/in', data)
export const stockOut = (data) => api.post('/stock/out', data)
export const getStockInRecords = (params) => api.get('/stock/in/records', { params })
export const getStockOutRecords = (params) => api.get('/stock/out/records', { params })

export default api
