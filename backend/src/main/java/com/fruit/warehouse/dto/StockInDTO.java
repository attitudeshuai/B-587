package com.fruit.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockInDTO {
    
    @NotNull(message = "水果ID不能为空")
    private Long fruitId;
    
    private Long supplierId;
    
    @NotNull(message = "入库数量不能为空")
    @Positive(message = "入库数量必须大于0")
    private BigDecimal quantity;
    
    @NotNull(message = "进货单价不能为空")
    @Positive(message = "进货单价必须大于0")
    private BigDecimal unitPrice;
    
    private Long operatorId;
    
    private String remark;
}
