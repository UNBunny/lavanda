package com.omstu.lavanda.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO для статистики заказов в LAVANDA ERP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatisticsDto {

    /**
     * Общее количество заказов
     */
    private long totalOrders;

    /**
     * Количество заказов за сегодня
     */
    private long todaysOrders;

    /**
     * Заказы, требующие обработки
     */
    private long ordersRequiringProcessing;

    /**
     * Заказы в процессе выполнения
     */
    private long ordersInProgress;

    /**
     * Заказы готовые к доставке
     */
    private long ordersReadyForDelivery;

    /**
     * Доставленные заказы
     */
    private long deliveredOrders;

    /**
     * Отмененные заказы
     */
    private long cancelledOrders;

    /**
     * Просроченные заказы
     */
    private long overdueOrders;

    /**
     * Общая выручка
     */
    private BigDecimal totalRevenue;

    /**
     * Выручка за сегодня
     */
    private BigDecimal todaysRevenue;

    /**
     * Средний чек
     */
    private BigDecimal averageOrderValue;

    /**
     * Распределение заказов по статусам
     */
    private Map<String, Long> ordersByStatus;

    /**
     * Топ флористов по количеству выполненных заказов
     */
    private Map<Long, Long> topFloristsByOrders;

    /**
     * Конверсия заказов (доставлено / создано) в процентах
     */
    private double conversionRate;
}
