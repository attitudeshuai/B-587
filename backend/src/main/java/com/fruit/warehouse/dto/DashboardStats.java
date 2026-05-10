package com.fruit.warehouse.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardStats {
    private long totalFruits;
    private long totalCategories;
    private long totalSuppliers;
    private long totalCustomers;
    private long todayStockIn;
    private long todayStockOut;
    private BigDecimal todayStockInAmount;
    private BigDecimal todayStockOutAmount;
    private List<LowStockFruit> lowStockFruits;
    private List<CategoryStock> categoryStocks;

    @Data
    public static class LowStockFruit {
        private Long id;
        private String name;
        private BigDecimal stockQuantity;
        private BigDecimal minStock;
        private String unit;
    }

    @Data
    public static class CategoryStock {
        private String categoryName;
        private long fruitCount;
    }
}
