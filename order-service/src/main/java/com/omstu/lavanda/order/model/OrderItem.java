package com.omstu.lavanda.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Позиция заказа в цветочной мастерской LAVANDA
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заказ, к которому относится позиция
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * ID товара из каталога
     */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /**
     * Название товара (сохраняем для истории)
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * Артикул товара
     */
    @Column(name = "product_sku")
    private String productSku;

    /**
     * Тип товара (цветок, материал, готовый букет, композиция)
     */
    @Column(name = "product_type")
    private String productType;

    /**
     * Количество товара
     */
    @Column(name = "quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    /**
     * Единица измерения (шт, м, кг и т.д.)
     */
    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    /**
     * Цена за единицу товара на момент заказа
     */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Общая стоимость позиции (количество * цена за единицу)
     */
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Скидка на позицию
     */
    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * Комментарий к позиции (особые пожелания клиента)
     */
    @Column(name = "notes")
    private String notes;

    /**
     * Является ли товар частью букета/композиции
     */
    @Column(name = "is_bouquet_component")
    @Builder.Default
    private Boolean isBouquetComponent = false;

    /**
     * ID родительского букета/композиции (если применимо)
     */
    @Column(name = "parent_bouquet_id")
    private Long parentBouquetId;

    /**
     * Пересчитать общую стоимость позиции
     */
    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        if (quantity != null && unitPrice != null) {
            this.totalPrice = quantity.multiply(unitPrice).subtract(
                discountAmount != null ? discountAmount : BigDecimal.ZERO
            );
        }
    }
}
