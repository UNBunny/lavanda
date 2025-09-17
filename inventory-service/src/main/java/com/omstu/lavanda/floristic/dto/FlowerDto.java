package com.omstu.lavanda.floristic.dto;

import com.omstu.lavanda.floristic.model.FlowerColor;
import com.omstu.lavanda.floristic.model.FlowerType;
import com.omstu.lavanda.floristic.model.SeasonalAvailability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для цветов в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowerDto {

    private Long id;
    
    private String name;
    
    private FlowerType type;
    
    private FlowerColor color;
    
    private String description;
    
    private BigDecimal pricePerPiece;
    
    private Integer stockQuantity;
    
    private Integer reservedQuantity;
    
    private Integer availableQuantity;
    
    private String supplier;
    
    private String supplierContact;
    
    private LocalDate expiryDate;
    
    private SeasonalAvailability seasonalAvailability;
    
    private String storageConditions;
    
    private String notes;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Дополнительные поля для удобства работы с API
    private String typeDisplayName;
    
    private String colorDisplayName;
    
    private String seasonalAvailabilityDisplayName;
    
    private Boolean isInSeason;
    
    private Boolean isLowStock;
    
    private Boolean isExpiringSoon;
}
