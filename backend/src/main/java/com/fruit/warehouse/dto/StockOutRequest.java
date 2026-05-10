package com.fruit.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockOutRequest {
    @NotNull(message = "水果ID不能为空")
    private Long fruitId;
    
    private Long customerId;
    
    @NotNull(message = "出库数量不能为空")
    @Positive(message = "出库数量必须大于0")
    private BigDecimal quantity;
    
    @NotNull(message = "批发单价不能为空")
    @Positive(message = "批发单价必须大于0")
    private BigDecimal unitPrice;
    
    private Long operatorId;
    
    private String remark;
}
