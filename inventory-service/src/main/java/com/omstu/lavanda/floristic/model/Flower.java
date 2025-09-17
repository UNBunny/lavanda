package com.omstu.lavanda.floristic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Модель цветка для флористического учета LAVANDA ERP
 * Учет ведется поштучно (розы, тюльпаны, лилии, хризантемы, пионы)
 */
@Entity
@Table(name = "flowers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "variety", length = 100)
    private String variety; // сорт (например, "Гран При", "Аваланж")

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FlowerType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private FlowerColor color;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock; // текущий остаток в штуках

    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock = 0; // зарезервировано под заказы

    @Column(name = "min_stock_level")
    private Integer minStockLevel; // минимальный уровень остатка

    @Column(name = "purchase_price", precision = 8, scale = 2)
    private BigDecimal purchasePrice; // закупочная цена за штуку

    @Column(name = "selling_price", precision = 8, scale = 2, nullable = false)
    private BigDecimal sellingPrice; // продажная цена за штуку

    @Column(name = "supplier", length = 200)
    private String supplier; // поставщик

    @Column(name = "country_origin", length = 100)
    private String countryOrigin; // страна происхождения

    @Column(name = "stem_length")
    private Integer stemLength; // длина стебля в см

    @Column(name = "freshness_days")
    private Integer freshnessDays; // срок свежести в днях

    @Column(name = "delivery_date")
    private LocalDate deliveryDate; // дата поступления

    @Column(name = "expiry_date")
    private LocalDate expiryDate; // дата истечения свежести

    @Enumerated(EnumType.STRING)
    @Column(name = "seasonal_availability")
    private SeasonalAvailability seasonalAvailability;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "notes", length = 500)
    private String notes; // дополнительные заметки

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Получить доступное количество (общий остаток - зарезервировано)
     */
    public Integer getAvailableStock() {
        return currentStock - reservedStock;
    }

    /**
     * Проверить, нужно ли пополнение запасов
     */
    public boolean needsRestock() {
        return minStockLevel != null && getAvailableStock() <= minStockLevel;
    }

    /**
     * Проверить свежесть цветка
     */
    public boolean isFresh() {
        return expiryDate == null || expiryDate.isAfter(LocalDate.now());
    }
}
