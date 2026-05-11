package com.fruit.warehouse.service;

import com.fruit.warehouse.dto.StockInRequest;
import com.fruit.warehouse.dto.StockOutRequest;
import com.fruit.warehouse.entity.*;
import com.fruit.warehouse.exception.BusinessException;
import com.fruit.warehouse.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class StockService {
    
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    
    private final StockInRecordRepository stockInRecordRepository;
    private final StockOutRecordRepository stockOutRecordRepository;
    private final FruitRepository fruitRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Transactional
    public StockInRecord stockIn(StockInRequest request) {
        logger.info("入库操作: 水果ID={}, 数量={}", request.getFruitId(), request.getQuantity());
        
        Fruit fruit = fruitRepository.findById(request.getFruitId())
                .orElseThrow(() -> new RuntimeException("水果不存在"));
        
        StockInRecord record = new StockInRecord();
        record.setRecordNo(generateRecordNo("IN"));
        record.setFruit(fruit);
        record.setQuantity(request.getQuantity());
        record.setUnitPrice(request.getUnitPrice());
        record.setTotalAmount(request.getQuantity().multiply(request.getUnitPrice()));
        record.setRemark(request.getRemark());
        
        if (request.getSupplierId() != null) {
            supplierRepository.findById(request.getSupplierId())
                    .ifPresent(record::setSupplier);
        }
        
        if (request.getOperatorId() != null) {
            userRepository.findById(request.getOperatorId())
                    .ifPresent(record::setOperator);
        }
        
        // 更新库存
        fruit.setStockQuantity(fruit.getStockQuantity().add(request.getQuantity()));
        fruit.setPurchasePrice(request.getUnitPrice());
        fruitRepository.save(fruit);
        
        return stockInRecordRepository.save(record);
    }

    @Transactional
    public StockOutRecord stockOut(StockOutRequest request) {
        logger.info("出库操作: 水果ID={}, 数量={}", request.getFruitId(), request.getQuantity());
        
        Fruit fruit = fruitRepository.findById(request.getFruitId())
                .orElseThrow(() -> new BusinessException(4004, "水果不存在"));
        
        if (fruit.getStockQuantity().compareTo(request.getQuantity()) < 0) {
            throw new BusinessException(4001, "库存不足");
        }
        
        StockOutRecord record = new StockOutRecord();
        record.setRecordNo(generateRecordNo("OUT"));
        record.setFruit(fruit);
        record.setQuantity(request.getQuantity());
        record.setUnitPrice(request.getUnitPrice());
        record.setTotalAmount(request.getQuantity().multiply(request.getUnitPrice()));
        record.setRemark(request.getRemark());
        
        if (request.getCustomerId() != null) {
            customerRepository.findById(request.getCustomerId())
                    .ifPresent(record::setCustomer);
        }
        
        if (request.getOperatorId() != null) {
            userRepository.findById(request.getOperatorId())
                    .ifPresent(record::setOperator);
        }
        
        // 更新库存
        fruit.setStockQuantity(fruit.getStockQuantity().subtract(request.getQuantity()));
        fruitRepository.save(fruit);
        
        return stockOutRecordRepository.save(record);
    }

    public Page<StockInRecord> findAllStockIn(Pageable pageable) {
        return stockInRecordRepository.findAll(pageable);
    }

    public Page<StockOutRecord> findAllStockOut(Pageable pageable) {
        return stockOutRecordRepository.findAll(pageable);
    }

    public long countTodayStockIn() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return stockInRecordRepository.countByCreatedAtAfter(startOfDay);
    }

    public long countTodayStockOut() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return stockOutRecordRepository.countByCreatedAtAfter(startOfDay);
    }

    public BigDecimal sumTodayStockInAmount() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        BigDecimal sum = stockInRecordRepository.sumTotalAmountBetween(start, end);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    public BigDecimal sumTodayStockOutAmount() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        BigDecimal sum = stockOutRecordRepository.sumTotalAmountBetween(start, end);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    private String generateRecordNo(String prefix) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return prefix + date + random;
    }
}
