package com.omstu.lavanda.floristic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для статистики по материалам в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialStatisticsDto {

    private Long totalMaterialTypes;
    
    private BigDecimal totalStockMeters;
    
    private BigDecimal totalReservedMeters;
    
    private BigDecimal totalAvailableMeters;
    
    private BigDecimal totalStockValue;
    
    private Long lowStockCount;
    
    private Long activeMaterialsCount;
    
    private Long inactiveMaterialsCount;
}
