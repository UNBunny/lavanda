package com.omstu.lavanda.floristic.mapper;

import com.omstu.lavanda.floristic.dto.CreateMaterialRequest;
import com.omstu.lavanda.floristic.dto.MaterialDto;
import com.omstu.lavanda.floristic.dto.UpdateMaterialRequest;
import com.omstu.lavanda.floristic.model.Material;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Маппер для преобразования между сущностями Material и DTO в флористическом учете LAVANDA ERP
 */
@Component
public class MaterialMapper {

    /**
     * Преобразование сущности Material в MaterialDto
     */
    public MaterialDto toDto(Material material) {
        if (material == null) {
            return null;
        }

        return MaterialDto.builder()
                .id(material.getId())
                .name(material.getName())
                .type(material.getType())
                .description(material.getDescription())
                .pricePerMeter(material.getPricePerMeter())
                .stockMeters(material.getStockMeters())
                .reservedMeters(material.getReservedMeters())
                .availableMeters(material.getAvailableMeters())
                .supplier(material.getSupplier())
                .supplierContact(material.getSupplierContact())
                .color(material.getColor())
                .width(material.getWidth())
                .material(material.getMaterial())
                .notes(material.getNotes())
                .isActive(material.getIsActive())
                .createdAt(material.getCreatedAt())
                .updatedAt(material.getUpdatedAt())
                // Дополнительные поля
                .typeDisplayName(material.getType() != null ? material.getType().getDisplayName() : null)
                .isLowStock(material.isLowStock())
                .totalValue(calculateTotalValue(material.getStockMeters(), material.getPricePerMeter()))
                .build();
    }

    /**
     * Преобразование CreateMaterialRequest в сущность Material
     */
    public Material toEntity(CreateMaterialRequest request) {
        if (request == null) {
            return null;
        }

        return Material.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .pricePerMeter(request.getPricePerMeter())
                .stockMeters(request.getStockMeters())
                .reservedMeters(BigDecimal.ZERO) // По умолчанию 0
                .supplier(request.getSupplier())
                .supplierContact(request.getSupplierContact())
                .color(request.getColor())
                .width(request.getWidth())
                .material(request.getMaterial())
                .notes(request.getNotes())
                .isActive(request.getIsActive())
                .build();
    }

    /**
     * Обновление сущности Material из UpdateMaterialRequest
     */
    public void updateEntity(Material material, UpdateMaterialRequest request) {
        if (material == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            material.setName(request.getName());
        }
        if (request.getType() != null) {
            material.setType(request.getType());
        }
        if (request.getDescription() != null) {
            material.setDescription(request.getDescription());
        }
        if (request.getPricePerMeter() != null) {
            material.setPricePerMeter(request.getPricePerMeter());
        }
        if (request.getSupplier() != null) {
            material.setSupplier(request.getSupplier());
        }
        if (request.getSupplierContact() != null) {
            material.setSupplierContact(request.getSupplierContact());
        }
        if (request.getColor() != null) {
            material.setColor(request.getColor());
        }
        if (request.getWidth() != null) {
            material.setWidth(request.getWidth());
        }
        if (request.getMaterial() != null) {
            material.setMaterial(request.getMaterial());
        }
        if (request.getNotes() != null) {
            material.setNotes(request.getNotes());
        }
        if (request.getIsActive() != null) {
            material.setIsActive(request.getIsActive());
        }
    }

    /**
     * Расчет общей стоимости материала на складе
     */
    private BigDecimal calculateTotalValue(BigDecimal stockMeters, BigDecimal pricePerMeter) {
        if (stockMeters == null || pricePerMeter == null) {
            return BigDecimal.ZERO;
        }
        return stockMeters.multiply(pricePerMeter);
    }
}
