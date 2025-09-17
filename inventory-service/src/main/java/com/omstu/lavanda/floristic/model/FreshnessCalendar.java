package com.omstu.lavanda.floristic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Календарь свежести цветов для флористического учета LAVANDA ERP
 * Отслеживает сроки годности и свежести цветов
 */
@Entity
@Table(name = "freshness_calendar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreshnessCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_id", nullable = false)
    private Flower flower;

    @Column(name = "batch_number", length = 50)
    private String batchNumber; // номер партии

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // количество цветов в партии

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate; // дата поступления

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate; // дата истечения свежести

    @Column(name = "days_until_expiry")
    private Integer daysUntilExpiry; // дней до истечения

    @Enumerated(EnumType.STRING)
    @Column(name = "freshness_status", nullable = false)
    private FreshnessStatus freshnessStatus;

    @Column(name = "storage_conditions", length = 200)
    private String storageConditions; // условия хранения

    @Column(name = "temperature_celsius")
    private Integer temperatureCelsius; // температура хранения

    @Column(name = "humidity_percentage")
    private Integer humidityPercentage; // влажность в процентах

    @Column(name = "is_sold", nullable = false)
    private Boolean isSold = false; // продано ли

    @Column(name = "sold_date")
    private LocalDate soldDate; // дата продажи

    @Column(name = "discount_percentage")
    private Integer discountPercentage; // скидка за близость к истечению

    @Column(name = "notes", length = 500)
    private String notes; // дополнительные заметки

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Обновить количество дней до истечения
     */
    @PrePersist
    @PreUpdate
    public void updateDaysUntilExpiry() {
        if (expiryDate != null) {
            this.daysUntilExpiry = (int) LocalDate.now().until(expiryDate).getDays();
            updateFreshnessStatus();
        }
    }

    /**
     * Обновить статус свежести на основе дней до истечения
     */
    private void updateFreshnessStatus() {
        if (daysUntilExpiry == null) {
            this.freshnessStatus = FreshnessStatus.UNKNOWN;
        } else if (daysUntilExpiry < 0) {
            this.freshnessStatus = FreshnessStatus.EXPIRED;
        } else if (daysUntilExpiry == 0) {
            this.freshnessStatus = FreshnessStatus.EXPIRES_TODAY;
        } else if (daysUntilExpiry <= 1) {
            this.freshnessStatus = FreshnessStatus.CRITICAL;
        } else if (daysUntilExpiry <= 3) {
            this.freshnessStatus = FreshnessStatus.WARNING;
        } else {
            this.freshnessStatus = FreshnessStatus.FRESH;
        }
    }

    /**
     * Проверить, нужна ли скидка
     */
    public boolean needsDiscount() {
        return freshnessStatus == FreshnessStatus.WARNING || 
               freshnessStatus == FreshnessStatus.CRITICAL;
    }

    /**
     * Получить рекомендуемую скидку в процентах
     */
    public Integer getRecommendedDiscount() {
        return switch (freshnessStatus) {
            case CRITICAL -> 50;
            case WARNING -> 25;
            case EXPIRES_TODAY -> 70;
            default -> 0;
        };
    }
}
