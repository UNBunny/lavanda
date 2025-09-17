package com.omstu.lavanda.floristic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для статистики по цветам в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowerStatisticsDto {

    private Long totalFlowerTypes;
    
    private Integer totalStockQuantity;
    
    private Integer totalReservedQuantity;
    
    private Integer totalAvailableQuantity;
    
    private BigDecimal totalStockValue;
    
    private Long lowStockCount;
    
    private Long expiringSoonCount;
    
    private Long expiredCount;
    
    private Long inSeasonCount;
    
    private Long outOfSeasonCount;
    
    private Long activeFlowersCount;
    
    private Long inactiveFlowersCount;
}
