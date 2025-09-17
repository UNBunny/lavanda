package com.omstu.lavanda.order.mapper;

import com.omstu.lavanda.order.dto.*;
import com.omstu.lavanda.order.model.Order;
import com.omstu.lavanda.order.model.OrderItem;
import com.omstu.lavanda.order.service.OrderStatistics;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования между Entity и DTO в Order Management Service
 */
@Component
public class OrderMapper {

    /**
     * Преобразовать Order в OrderDto
     */
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerEmail(order.getCustomerEmail())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryDate(order.getDeliveryDate())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .notes(order.getNotes())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .assignedFloristId(order.getAssignedFloristId())
                .items(order.getItems().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    /**
     * Преобразовать OrderItem в OrderItemDto
     */
    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .productSku(orderItem.getProductSku())
                .productType(orderItem.getProductType())
                .quantity(orderItem.getQuantity())
                .unitOfMeasure(orderItem.getUnitOfMeasure())
                .unitPrice(orderItem.getUnitPrice())
                .totalPrice(orderItem.getTotalPrice())
                .discountAmount(orderItem.getDiscountAmount())
                .notes(orderItem.getNotes())
                .isBouquetComponent(orderItem.getIsBouquetComponent())
                .parentBouquetId(orderItem.getParentBouquetId())
                .build();
    }

    /**
     * Преобразовать CreateOrderRequest в Order
     */
    public Order toEntity(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }

        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .customerEmail(request.getCustomerEmail())
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryDate(request.getDeliveryDate())
                .discountAmount(request.getDiscountAmount() != null ? 
                        request.getDiscountAmount() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .paymentMethod(request.getPaymentMethod())
                .assignedFloristId(request.getAssignedFloristId())
                .build();

        // Преобразуем позиции заказа
        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> toEntity(itemRequest, order))
                .collect(Collectors.toList());
        
        order.setItems(items);
        
        return order;
    }

    /**
     * Преобразовать CreateOrderItemRequest в OrderItem
     */
    public OrderItem toEntity(CreateOrderItemRequest request, Order order) {
        if (request == null) {
            return null;
        }

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .productId(request.getProductId())
                .productName(request.getProductName())
                .productSku(request.getProductSku())
                .productType(request.getProductType())
                .quantity(request.getQuantity())
                .unitOfMeasure(request.getUnitOfMeasure())
                .unitPrice(request.getUnitPrice())
                .discountAmount(request.getDiscountAmount() != null ? 
                        request.getDiscountAmount() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .isBouquetComponent(request.getIsBouquetComponent() != null ? 
                        request.getIsBouquetComponent() : false)
                .parentBouquetId(request.getParentBouquetId())
                .build();

        // Рассчитываем общую стоимость позиции
        orderItem.calculateTotalPrice();
        
        return orderItem;
    }

    /**
     * Преобразовать OrderStatistics в OrderStatisticsDto
     */
    public OrderStatisticsDto toDto(OrderStatistics statistics) {
        if (statistics == null) {
            return null;
        }

        return OrderStatisticsDto.builder()
                .totalOrders(statistics.getTotalOrders())
                .todaysOrders(statistics.getTodaysOrders())
                .ordersRequiringProcessing(statistics.getOrdersRequiringProcessing())
                .ordersInProgress(statistics.getOrdersInProgress())
                .ordersReadyForDelivery(statistics.getOrdersReadyForDelivery())
                .deliveredOrders(statistics.getDeliveredOrders())
                .cancelledOrders(statistics.getCancelledOrders())
                .overdueOrders(statistics.getOverdueOrders())
                .totalRevenue(statistics.getTotalRevenue())
                .todaysRevenue(statistics.getTodaysRevenue())
                .averageOrderValue(statistics.getAverageOrderValue())
                .ordersByStatus(statistics.getOrdersByStatus())
                .topFloristsByOrders(statistics.getTopFloristsByOrders())
                .conversionRate(statistics.getConversionRate())
                .build();
    }

    /**
     * Преобразовать список Order в список OrderDto
     */
    public List<OrderDto> toDtoList(List<Order> orders) {
        if (orders == null) {
            return null;
        }

        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Обновить существующий Order данными из OrderDto
     */
    public void updateEntity(Order existingOrder, OrderDto orderDto) {
        if (existingOrder == null || orderDto == null) {
            return;
        }

        existingOrder.setCustomerName(orderDto.getCustomerName());
        existingOrder.setCustomerPhone(orderDto.getCustomerPhone());
        existingOrder.setCustomerEmail(orderDto.getCustomerEmail());
        existingOrder.setDeliveryAddress(orderDto.getDeliveryAddress());
        existingOrder.setDeliveryDate(orderDto.getDeliveryDate());
        existingOrder.setDiscountAmount(orderDto.getDiscountAmount());
        existingOrder.setNotes(orderDto.getNotes());
        existingOrder.setPaymentMethod(orderDto.getPaymentMethod());
        existingOrder.setPaymentStatus(orderDto.getPaymentStatus());
        existingOrder.setAssignedFloristId(orderDto.getAssignedFloristId());

        // Обновляем позиции заказа (упрощенная версия)
        // В реальном приложении здесь должна быть более сложная логика
        // для обработки добавления, удаления и изменения позиций
        existingOrder.getItems().clear();
        if (orderDto.getItems() != null) {
            List<OrderItem> updatedItems = orderDto.getItems().stream()
                    .map(itemDto -> {
                        OrderItem item = OrderItem.builder()
                                .order(existingOrder)
                                .productId(itemDto.getProductId())
                                .productName(itemDto.getProductName())
                                .productSku(itemDto.getProductSku())
                                .productType(itemDto.getProductType())
                                .quantity(itemDto.getQuantity())
                                .unitOfMeasure(itemDto.getUnitOfMeasure())
                                .unitPrice(itemDto.getUnitPrice())
                                .discountAmount(itemDto.getDiscountAmount())
                                .notes(itemDto.getNotes())
                                .isBouquetComponent(itemDto.getIsBouquetComponent())
                                .parentBouquetId(itemDto.getParentBouquetId())
                                .build();
                        item.calculateTotalPrice();
                        return item;
                    })
                    .collect(Collectors.toList());
            existingOrder.getItems().addAll(updatedItems);
        }

        // Пересчитываем общую стоимость заказа
        existingOrder.calculateTotalAmount();
    }
}
