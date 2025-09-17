package com.omstu.lavanda.floristic.dto;

import com.omstu.lavanda.floristic.model.FreshnessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для календаря свежести в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreshnessCalendarDto {

    private Long id;
    
    private Long flowerId;
    
    private String flowerName;
    
    private String batchNumber;
    
    private Integer quantity;
    
    private LocalDate deliveryDate;
    
    private LocalDate expiryDate;
    
    private FreshnessStatus freshnessStatus;
    
    private String storageConditions;
    
    private Integer temperatureCelsius;
    
    private Integer humidityPercentage;
    
    private Integer discountPercentage;
    
    private Boolean isSold;
    
    private LocalDate soldDate;
    
    private String notes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Дополнительные поля для удобства работы с API
    private String freshnessStatusDisplayName;
    
    private Integer daysUntilExpiry;
    
    private Boolean isExpired;
    
    private Boolean isExpiringSoon;
    
    private Integer recommendedDiscount;
    
    private Boolean needsDiscount;
}
