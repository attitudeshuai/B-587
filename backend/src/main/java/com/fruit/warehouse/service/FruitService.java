package com.fruit.warehouse.service;

import com.fruit.warehouse.entity.Fruit;
import com.fruit.warehouse.repository.FruitRepository;
import com.fruit.warehouse.repository.StockInRecordRepository;
import com.fruit.warehouse.repository.StockOutRecordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FruitService {
    
    private static final Logger logger = LoggerFactory.getLogger(FruitService.class);
    
    private final FruitRepository fruitRepository;
    private final StockInRecordRepository stockInRecordRepository;
    private final StockOutRecordRepository stockOutRecordRepository;

    public Page<Fruit> findAll(Pageable pageable) {
        return fruitRepository.findAll(pageable);
    }

    public List<Fruit> findAll() {
        return fruitRepository.findAll();
    }

    public Optional<Fruit> findById(Long id) {
        return fruitRepository.findById(id);
    }

    public Page<Fruit> searchFruits(String name, Long categoryId, Integer status, Pageable pageable) {
        return fruitRepository.searchFruits(name, categoryId, status, pageable);
    }

    public List<Fruit> findLowStockFruits() {
        return fruitRepository.findLowStockFruits();
    }

    public Fruit save(Fruit fruit) {
        logger.info("创建/更新水果: {}", fruit.getName());
        return fruitRepository.save(fruit);
    }

    public void deleteById(Long id) {
        logger.info("删除水果: {}", id);
        // 检查是否存在关联的入库记录
        if (stockInRecordRepository.existsByFruitId(id)) {
            throw new IllegalArgumentException("删除失败：该水果存在入库记录，无法删除。如需删除，请先删除相关入库记录");
        }
        // 检查是否存在关联的出库记录
        if (stockOutRecordRepository.existsByFruitId(id)) {
            throw new IllegalArgumentException("删除失败：该水果存在出库记录，无法删除。如需删除，请先删除相关出库记录");
        }
        fruitRepository.deleteById(id);
    }

    public long count() {
        return fruitRepository.count();
    }

    public long countByStatus(Integer status) {
        return fruitRepository.countByStatus(status);
    }
}
