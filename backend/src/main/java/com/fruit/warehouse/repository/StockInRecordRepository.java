package com.fruit.warehouse.repository;

import com.fruit.warehouse.entity.StockInRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockInRecordRepository extends JpaRepository<StockInRecord, Long> {
    
    Page<StockInRecord> findByFruitId(Long fruitId, Pageable pageable);
    
    List<StockInRecord> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT SUM(s.totalAmount) FROM StockInRecord s WHERE s.createdAt >= :start AND s.createdAt <= :end")
    BigDecimal sumTotalAmountBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(s) FROM StockInRecord s WHERE s.createdAt >= :start")
    long countByCreatedAtAfter(LocalDateTime start);
    
    // 检查是否存在关联记录
    boolean existsByFruitId(Long fruitId);
    
    boolean existsBySupplierId(Long supplierId);
}
