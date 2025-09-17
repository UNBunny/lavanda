package com.omstu.lavanda.floristic.dto;

import com.omstu.lavanda.floristic.model.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для материалов в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDto {

    private Long id;
    
    private String name;
    
    private MaterialType type;
    
    private String description;
    
    private BigDecimal pricePerMeter;
    
    private BigDecimal stockMeters;
    
    private BigDecimal reservedMeters;
    
    private BigDecimal availableMeters;
    
    private String supplier;
    
    private String supplierContact;
    
    private String color;
    
    private String width;
    
    private String material;
    
    private String notes;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Дополнительные поля для удобства работы с API
    private String typeDisplayName;
    
    private Boolean isLowStock;
    
    private BigDecimal totalValue;
}
