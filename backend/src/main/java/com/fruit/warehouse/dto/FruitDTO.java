package com.fruit.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FruitDTO {
    
    private Long id;
    
    @NotBlank(message = "水果名称不能为空")
    private String name;
    
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    
    private String unit = "斤";
    
    @NotNull(message = "进货价格不能为空")
    @Positive(message = "进货价格必须大于0")
    private BigDecimal purchasePrice;
    
    @NotNull(message = "批发价格不能为空")
    @Positive(message = "批发价格必须大于0")
    private BigDecimal salePrice;
    
    private BigDecimal stockQuantity;
    
    private BigDecimal minStock;
    
    private String origin;
    
    private String description;
    
    private String imageUrl;
    
    private Integer status = 1;
}
