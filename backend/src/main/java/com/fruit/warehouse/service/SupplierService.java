package com.fruit.warehouse.service;

import com.fruit.warehouse.entity.Supplier;
import com.fruit.warehouse.repository.SupplierRepository;
import com.fruit.warehouse.repository.StockInRecordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    
    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);
    
    private final SupplierRepository supplierRepository;
    private final StockInRecordRepository stockInRecordRepository;

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public List<Supplier> findByStatus(Integer status) {
        return supplierRepository.findByStatus(status);
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier save(Supplier supplier) {
        logger.info("创建/更新供应商: {}", supplier.getName());
        return supplierRepository.save(supplier);
    }

    public void deleteById(Long id) {
        logger.info("删除供应商: {}", id);
        // 检查是否存在关联的入库记录
        if (stockInRecordRepository.existsBySupplierId(id)) {
            throw new IllegalArgumentException("删除失败：该供应商存在入库记录，无法删除。如需删除，请先删除相关入库记录");
        }
        supplierRepository.deleteById(id);
    }

    public long count() {
        return supplierRepository.count();
    }
}
