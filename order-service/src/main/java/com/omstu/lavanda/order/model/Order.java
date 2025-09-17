package com.omstu.lavanda.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность заказа в цветочной мастерской LAVANDA
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Номер заказа (уникальный идентификатор для клиентов)
     */
    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    /**
     * Статус заказа
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    /**
     * Имя клиента
     */
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    /**
     * Телефон клиента
     */
    @Column(name = "customer_phone")
    private String customerPhone;

    /**
     * Email клиента
     */
    @Column(name = "customer_email")
    private String customerEmail;

    /**
     * Адрес доставки
     */
    @Column(name = "delivery_address")
    private String deliveryAddress;

    /**
     * Желаемая дата и время доставки
     */
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    /**
     * Общая стоимость заказа
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Размер скидки
     */
    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * Итоговая стоимость с учетом скидки
     */
    @Column(name = "final_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    /**
     * Комментарий к заказу
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Способ оплаты
     */
    @Column(name = "payment_method")
    private String paymentMethod;

    /**
     * Статус оплаты
     */
    @Column(name = "payment_status")
    private String paymentStatus;

    /**
     * ID флориста, назначенного на заказ
     */
    @Column(name = "assigned_florist_id")
    private Long assignedFloristId;

    /**
     * Позиции заказа
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Дата создания заказа
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата последнего обновления заказа
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Добавить позицию к заказу
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Удалить позицию из заказа
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    /**
     * Пересчитать общую стоимость заказа
     */
    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.finalAmount = this.totalAmount.subtract(this.discountAmount);
    }
}
