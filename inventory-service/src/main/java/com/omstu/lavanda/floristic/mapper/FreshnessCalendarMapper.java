package com.omstu.lavanda.floristic.mapper;

import com.omstu.lavanda.floristic.dto.FreshnessCalendarDto;
import com.omstu.lavanda.floristic.model.FreshnessCalendar;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Маппер для преобразования между сущностями FreshnessCalendar и DTO в флористическом учете LAVANDA ERP
 */
@Component
public class FreshnessCalendarMapper {

    /**
     * Преобразование сущности FreshnessCalendar в FreshnessCalendarDto
     */
    public FreshnessCalendarDto toDto(FreshnessCalendar freshnessCalendar) {
        if (freshnessCalendar == null) {
            return null;
        }

        return FreshnessCalendarDto.builder()
                .id(freshnessCalendar.getId())
                .flowerId(freshnessCalendar.getFlower() != null ? freshnessCalendar.getFlower().getId() : null)
                .flowerName(freshnessCalendar.getFlower() != null ? freshnessCalendar.getFlower().getName() : null)
                .batchNumber(freshnessCalendar.getBatchNumber())
                .quantity(freshnessCalendar.getQuantity())
                .deliveryDate(freshnessCalendar.getDeliveryDate())
                .expiryDate(freshnessCalendar.getExpiryDate())
                .freshnessStatus(freshnessCalendar.getFreshnessStatus())
                .storageConditions(freshnessCalendar.getStorageConditions())
                .temperatureCelsius(freshnessCalendar.getTemperatureCelsius())
                .humidityPercentage(freshnessCalendar.getHumidityPercentage())
                .discountPercentage(freshnessCalendar.getDiscountPercentage())
                .isSold(freshnessCalendar.getIsSold())
                .soldDate(freshnessCalendar.getSoldDate())
                .notes(freshnessCalendar.getNotes())
                .createdAt(freshnessCalendar.getCreatedAt())
                .updatedAt(freshnessCalendar.getUpdatedAt())
                // Дополнительные поля
                .freshnessStatusDisplayName(freshnessCalendar.getFreshnessStatus() != null ? 
                        freshnessCalendar.getFreshnessStatus().getDisplayName() : null)
                .daysUntilExpiry(calculateDaysUntilExpiry(freshnessCalendar.getExpiryDate()))
                .isExpired(isExpired(freshnessCalendar.getExpiryDate()))
                .isExpiringSoon(isExpiringSoon(freshnessCalendar.getExpiryDate()))
                .recommendedDiscount(freshnessCalendar.getRecommendedDiscount())
                .needsDiscount(freshnessCalendar.needsDiscount())
                .build();
    }

    /**
     * Расчет количества дней до истечения срока
     */
    private Integer calculateDaysUntilExpiry(LocalDate expiryDate) {
        if (expiryDate == null) {
            return null;
        }
        
        LocalDate now = LocalDate.now();
        long days = ChronoUnit.DAYS.between(now, expiryDate);
        return (int) days;
    }

    /**
     * Проверка, истек ли срок годности
     */
    private Boolean isExpired(LocalDate expiryDate) {
        if (expiryDate == null) {
            return false;
        }
        
        return expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Проверка, истекает ли срок в ближайшие 3 дня
     */
    private Boolean isExpiringSoon(LocalDate expiryDate) {
        if (expiryDate == null) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        long daysUntilExpiry = ChronoUnit.DAYS.between(now, expiryDate);
        return daysUntilExpiry >= 0 && daysUntilExpiry <= 3;
    }
}
