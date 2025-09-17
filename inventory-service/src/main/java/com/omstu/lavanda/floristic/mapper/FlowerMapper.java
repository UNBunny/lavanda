package com.omstu.lavanda.floristic.mapper;

import com.omstu.lavanda.floristic.dto.CreateFlowerRequest;
import com.omstu.lavanda.floristic.dto.FlowerDto;
import com.omstu.lavanda.floristic.dto.UpdateFlowerRequest;
import com.omstu.lavanda.floristic.model.Flower;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Маппер для преобразования между сущностями Flower и DTO в флористическом учете LAVANDA ERP
 */
@Component
public class FlowerMapper {

    /**
     * Преобразование сущности Flower в FlowerDto
     */
    public FlowerDto toDto(Flower flower) {
        if (flower == null) {
            return null;
        }

        return FlowerDto.builder()
                .id(flower.getId())
                .name(flower.getName())
                .type(flower.getType())
                .color(flower.getColor())
                .variety(flower.getVariety()) // исправлено: было getDescription()
                .pricePerPiece(flower.getSellingPrice()) // исправлено: было getPricePerPiece()
                .stockQuantity(flower.getCurrentStock()) // исправлено: было getStockQuantity()
                .reservedQuantity(flower.getReservedStock()) // исправлено: было getReservedQuantity()
                .availableQuantity(flower.getAvailableStock()) // исправлено: было getAvailableQuantity()
                .supplier(flower.getSupplier())
                .countryOrigin(flower.getCountryOrigin()) // добавлено новое поле
                .stemLength(flower.getStemLength()) // добавлено новое поле
                .freshnessDays(flower.getFreshnessDays()) // добавлено новое поле
                .deliveryDate(flower.getDeliveryDate()) // добавлено новое поле
                .expiryDate(flower.getExpiryDate())
                .seasonalAvailability(flower.getSeasonalAvailability())
                .minStockLevel(flower.getMinStockLevel()) // добавлено новое поле
                .purchasePrice(flower.getPurchasePrice()) // добавлено новое поле
                .notes(flower.getNotes())
                .isActive(flower.getIsActive())
                .createdAt(flower.getCreatedAt())
                .updatedAt(flower.getUpdatedAt())
                // Дополнительные поля
                .typeDisplayName(flower.getType() != null ? flower.getType().getDisplayName() : null)
                .colorDisplayName(flower.getColor() != null ? flower.getColor().getDisplayName() : null)
                .seasonalAvailabilityDisplayName(flower.getSeasonalAvailability() != null ?
                        flower.getSeasonalAvailability().getDisplayName() : null)
                .isInSeason(flower.isInSeason())
                .isLowStock(flower.needsRestock()) // используем метод из модели
                .isExpiringSoon(isExpiringSoon(flower.getExpiryDate()))
                .isFresh(flower.isFresh()) // используем метод из модели
                .build();
    }

    /**
     * Преобразование CreateFlowerRequest в сущность Flower
     */
    public Flower toEntity(CreateFlowerRequest request) {
        if (request == null) {
            return null;
        }

        return Flower.builder()
                .name(request.getName())
                .type(request.getType())
                .color(request.getColor())
                .variety(request.getVariety()) // исправлено: было getDescription()
                .sellingPrice(request.getPricePerPiece()) // исправлено: было getPricePerPiece()
                .currentStock(request.getStockQuantity()) // исправлено: было getStockQuantity()
                .reservedStock(0) // По умолчанию 0
                .supplier(request.getSupplier())
                .countryOrigin(request.getCountryOrigin()) // добавлено новое поле
                .stemLength(request.getStemLength()) // добавлено новое поле
                .freshnessDays(request.getFreshnessDays()) // добавлено новое поле
                .deliveryDate(request.getDeliveryDate()) // добавлено новое поле
                .expiryDate(request.getExpiryDate())
                .seasonalAvailability(request.getSeasonalAvailability())
                .minStockLevel(request.getMinStockLevel()) // добавлено новое поле
                .purchasePrice(request.getPurchasePrice()) // добавлено новое поле
                .notes(request.getNotes())
                .isActive(request.getIsActive())
                .build();
    }

    /**
     * Обновление сущности Flower из UpdateFlowerRequest
     */
    public void updateEntity(Flower flower, UpdateFlowerRequest request) {
        if (flower == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            flower.setName(request.getName());
        }
        if (request.getType() != null) {
            flower.setType(request.getType());
        }
        if (request.getColor() != null) {
            flower.setColor(request.getColor());
        }
        if (request.getVariety() != null) { // исправлено: было getDescription()
            flower.setVariety(request.getVariety()); // исправлено: было setDescription()
        }
        if (request.getPricePerPiece() != null) {
            flower.setSellingPrice(request.getPricePerPiece()); // исправлено: было setPricePerPiece()
        }
        if (request.getSupplier() != null) {
            flower.setSupplier(request.getSupplier());
        }
        if (request.getCountryOrigin() != null) { // добавлено новое поле
            flower.setCountryOrigin(request.getCountryOrigin());
        }
        if (request.getStemLength() != null) { // добавлено новое поле
            flower.setStemLength(request.getStemLength());
        }
        if (request.getFreshnessDays() != null) { // добавлено новое поле
            flower.setFreshnessDays(request.getFreshnessDays());
        }
        if (request.getDeliveryDate() != null) { // добавлено новое поле
            flower.setDeliveryDate(request.getDeliveryDate());
        }
        if (request.getExpiryDate() != null) {
            flower.setExpiryDate(request.getExpiryDate());
        }
        if (request.getSeasonalAvailability() != null) {
            flower.setSeasonalAvailability(request.getSeasonalAvailability());
        }
        if (request.getMinStockLevel() != null) { // добавлено новое поле
            flower.setMinStockLevel(request.getMinStockLevel());
        }
        if (request.getPurchasePrice() != null) { // добавлено новое поле
            flower.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getNotes() != null) {
            flower.setNotes(request.getNotes());
        }
        if (request.getIsActive() != null) {
            flower.setIsActive(request.getIsActive());
        }
    }

    /**
     * Проверка, истекает ли цветок в ближайшие 3 дня
     */
    private boolean isExpiringSoon(LocalDate expiryDate) {
        if (expiryDate == null) {
            return false;
        }

        LocalDate now = LocalDate.now();
        long daysUntilExpiry = ChronoUnit.DAYS.between(now, expiryDate);
        return daysUntilExpiry >= 0 && daysUntilExpiry <= 3;
    }
}