package com.fruit.warehouse.service;

import com.fruit.warehouse.dto.DashboardStats;
import com.fruit.warehouse.entity.Category;
import com.fruit.warehouse.entity.Fruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final FruitService fruitService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final CustomerService customerService;
    private final StockService stockService;

    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        
        stats.setTotalFruits(fruitService.count());
        stats.setTotalCategories(categoryService.count());
        stats.setTotalSuppliers(supplierService.count());
        stats.setTotalCustomers(customerService.count());
        stats.setTodayStockIn(stockService.countTodayStockIn());
        stats.setTodayStockOut(stockService.countTodayStockOut());
        stats.setTodayStockInAmount(stockService.sumTodayStockInAmount());
        stats.setTodayStockOutAmount(stockService.sumTodayStockOutAmount());
        
        // 低库存预警
        List<DashboardStats.LowStockFruit> lowStockFruits = fruitService.findLowStockFruits()
                .stream()
                .map(fruit -> {
                    DashboardStats.LowStockFruit lsf = new DashboardStats.LowStockFruit();
                    lsf.setId(fruit.getId());
                    lsf.setName(fruit.getName());
                    lsf.setStockQuantity(fruit.getStockQuantity());
                    lsf.setMinStock(fruit.getMinStock());
                    lsf.setUnit(fruit.getUnit());
                    return lsf;
                })
                .collect(Collectors.toList());
        stats.setLowStockFruits(lowStockFruits);
        
        // 分类统计
        List<Category> categories = categoryService.findAll();
        List<Fruit> allFruits = fruitService.findAll();
        List<DashboardStats.CategoryStock> categoryStocks = categories.stream()
                .map(cat -> {
                    DashboardStats.CategoryStock cs = new DashboardStats.CategoryStock();
                    cs.setCategoryName(cat.getName());
                    cs.setFruitCount(allFruits.stream()
                            .filter(f -> f.getCategory().getId().equals(cat.getId()))
                            .count());
                    return cs;
                })
                .collect(Collectors.toList());
        stats.setCategoryStocks(categoryStocks);
        
        return stats;
    }
}
