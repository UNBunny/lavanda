package com.omstu.lavanda.floristic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Модель материала для флористического учета LAVANDA ERP
 * Учет ведется по метрам (ленты, упаковка, проволока)
 */
@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MaterialType type;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "width_mm")
    private Integer widthMm; // ширина в миллиметрах

    @Column(name = "current_stock_meters", precision = 10, scale = 3, nullable = false)
    private BigDecimal currentStockMeters; // текущий остаток в метрах

    @Column(name = "reserved_stock_meters", precision = 10, scale = 3, nullable = false)
    private BigDecimal reservedStockMeters = BigDecimal.ZERO; // зарезервировано под заказы

    @Column(name = "min_stock_level_meters", precision = 10, scale = 3)
    private BigDecimal minStockLevelMeters; // минимальный уровень остатка

    @Column(name = "purchase_price_per_meter", precision = 8, scale = 2)
    private BigDecimal purchasePricePerMeter; // закупочная цена за метр

    @Column(name = "selling_price_per_meter", precision = 8, scale = 2, nullable = false)
    private BigDecimal sellingPricePerMeter; // продажная цена за метр

    @Column(name = "supplier", length = 200)
    private String supplier; // поставщик

    @Column(name = "material_composition", length = 200)
    private String materialComposition; // состав материала (атлас, органза, джут)

    @Column(name = "texture", length = 100)
    private String texture; // текстура (матовая, глянцевая, с блестками)

    @Column(name = "is_waterproof")
    private Boolean isWaterproof = false; // водостойкость

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
     * Получить доступное количество в метрах (общий остаток - зарезервировано)
     */
    public BigDecimal getAvailableStockMeters() {
        return currentStockMeters.subtract(reservedStockMeters);
    }

    /**
     * Проверить, нужно ли пополнение запасов
     */
    public boolean needsRestock() {
        return minStockLevelMeters != null && 
               getAvailableStockMeters().compareTo(minStockLevelMeters) <= 0;
    }

    /**
     * Получить общую стоимость остатка
     */
    public BigDecimal getTotalStockValue() {
        if (purchasePricePerMeter == null) {
            return BigDecimal.ZERO;
        }
        return currentStockMeters.multiply(purchasePricePerMeter);
    }
}
