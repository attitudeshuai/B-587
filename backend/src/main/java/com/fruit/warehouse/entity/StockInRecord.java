package com.fruit.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stock_in_records")
public class StockInRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "record_no", nullable = false, unique = true, length = 50)
    private String recordNo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fruit_id", nullable = false)
    private Fruit fruit;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id")
    private User operator;
    
    @Column(columnDefinition = "TEXT")
    private String remark;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
