package com.omstu.lavanda.order.service.impl;

import com.omstu.lavanda.order.model.Order;
import com.omstu.lavanda.order.model.OrderStatus;
import com.omstu.lavanda.order.repository.OrderRepository;
import com.omstu.lavanda.order.service.OrderService;
import com.omstu.lavanda.order.service.OrderStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления заказами для LAVANDA ERP
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        log.info("Создание нового заказа для клиента: {}", order.getCustomerName());
        
        // Генерируем номер заказа если не указан
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        }
        
        // Устанавливаем статус "Новый" если не указан
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.NEW);
        }
        
        // Пересчитываем стоимость заказа
        order.calculateTotalAmount();
        
        Order savedOrder = orderRepository.save(order);
        log.info("Заказ создан с ID: {} и номером: {}", savedOrder.getId(), savedOrder.getOrderNumber());
        
        return savedOrder;
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        log.debug("Поиск заказа по ID: {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public Optional<Order> getOrderByNumber(String orderNumber) {
        log.debug("Поиск заказа по номеру: {}", orderNumber);
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        log.debug("Получение всех заказов с пагинацией: {}", pageable);
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        log.debug("Поиск заказов по статусу: {}", status);
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> getOrdersByCustomerPhone(String customerPhone) {
        log.debug("Поиск заказов клиента по телефону: {}", customerPhone);
        return orderRepository.findByCustomerPhoneOrderByCreatedAtDesc(customerPhone);
    }

    @Override
    public List<Order> getOrdersByCustomerEmail(String customerEmail) {
        log.debug("Поиск заказов клиента по email: {}", customerEmail);
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(customerEmail);
    }

    @Override
    @Transactional
    public Order updateOrder(Order order) {
        log.info("Обновление заказа с ID: {}", order.getId());
        
        // Пересчитываем стоимость заказа
        order.calculateTotalAmount();
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Заказ обновлен: {}", updatedOrder.getOrderNumber());
        
        return updatedOrder;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        log.info("Изменение статуса заказа {} на {}", orderId, newStatus);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        
        OrderStatus currentStatus = order.getStatus();
        
        if (!canChangeOrderStatus(currentStatus, newStatus)) {
            throw new RuntimeException(
                String.format("Невозможно изменить статус с %s на %s", currentStatus, newStatus)
            );
        }
        
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Статус заказа {} изменен с {} на {}", 
                order.getOrderNumber(), currentStatus, newStatus);
        
        return updatedOrder;
    }

    @Override
    @Transactional
    public Order assignFloristToOrder(Long orderId, Long floristId) {
        log.info("Назначение флориста {} на заказ {}", floristId, orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        
        order.setAssignedFloristId(floristId);
        
        // Если заказ был новый или подтвержденный, переводим в работу
        if (order.getStatus() == OrderStatus.NEW || order.getStatus() == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.IN_PROGRESS);
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Флорист {} назначен на заказ {}", floristId, order.getOrderNumber());
        
        return updatedOrder;
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId, String reason) {
        log.info("Отмена заказа {} по причине: {}", orderId, reason);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        
        // Проверяем, можно ли отменить заказ
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Нельзя отменить доставленный заказ");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        if (reason != null && !reason.isEmpty()) {
            String currentNotes = order.getNotes() != null ? order.getNotes() : "";
            order.setNotes(currentNotes + "\nПричина отмены: " + reason);
        }
        
        Order cancelledOrder = orderRepository.save(order);
        log.info("Заказ {} отменен", order.getOrderNumber());
        
        return cancelledOrder;
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        log.info("Удаление заказа {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        
        // Проверяем, можно ли удалить заказ
        if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.NEW) {
            throw new RuntimeException("Можно удалять только новые или отмененные заказы");
        }
        
        orderRepository.delete(order);
        log.info("Заказ {} удален", order.getOrderNumber());
    }

    @Override
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Поиск заказов за период с {} по {}", startDate, endDate);
        return orderRepository.findOrdersByDateRange(startDate, endDate);
    }

    @Override
    public List<Order> getOrdersByDeliveryDate(LocalDateTime deliveryDate) {
        log.debug("Поиск заказов с доставкой на {}", deliveryDate);
        return orderRepository.findOrdersByDeliveryDate(deliveryDate);
    }

    @Override
    public List<Order> getOrdersRequiringProcessing() {
        log.debug("Получение заказов, требующих обработки");
        return orderRepository.findOrdersRequiringProcessing();
    }

    @Override
    public List<Order> getActiveOrdersByFlorist(Long floristId) {
        log.debug("Получение активных заказов флориста {}", floristId);
        return orderRepository.findActiveOrdersByFlorist(floristId);
    }

    @Override
    public List<Order> getOrdersReadyForDelivery() {
        log.debug("Получение заказов готовых к доставке");
        return orderRepository.findOrdersReadyForDelivery();
    }

    @Override
    public List<Order> getOverdueOrders() {
        log.debug("Получение просроченных заказов");
        return orderRepository.findOverdueOrders(LocalDateTime.now());
    }

    @Override
    public Page<Order> searchOrders(String searchTerm, Pageable pageable) {
        log.debug("Поиск заказов по термину: {}", searchTerm);
        return orderRepository.searchOrders(searchTerm, pageable);
    }

    @Override
    public OrderStatistics getOrderStatistics() {
        log.debug("Получение общей статистики заказов");
        return getOrderStatisticsByDateRange(
                LocalDateTime.now().minusYears(1), 
                LocalDateTime.now()
        );
    }

    @Override
    public OrderStatistics getOrderStatisticsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Получение статистики заказов за период с {} по {}", startDate, endDate);
        
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        
        long totalOrders = orders.size();
        long todaysOrders = orderRepository.countTodaysOrders();
        
        Map<OrderStatus, Long> statusCounts = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
        
        BigDecimal totalRevenue = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .map(Order::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal todaysRevenue = orders.stream()
                .filter(order -> order.getCreatedAt().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .map(Order::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageOrderValue = totalOrders > 0 ? 
                totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;
        
        long deliveredOrders = statusCounts.getOrDefault(OrderStatus.DELIVERED, 0L);
        double conversionRate = totalOrders > 0 ? 
                (double) deliveredOrders / totalOrders * 100 : 0.0;
        
        Map<String, Long> ordersByStatus = statusCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getDisplayName(),
                        Map.Entry::getValue
                ));
        
        return OrderStatistics.builder()
                .totalOrders(totalOrders)
                .todaysOrders(todaysOrders)
                .ordersRequiringProcessing(statusCounts.getOrDefault(OrderStatus.NEW, 0L) + 
                                         statusCounts.getOrDefault(OrderStatus.CONFIRMED, 0L))
                .ordersInProgress(statusCounts.getOrDefault(OrderStatus.IN_PROGRESS, 0L))
                .ordersReadyForDelivery(statusCounts.getOrDefault(OrderStatus.READY, 0L))
                .deliveredOrders(deliveredOrders)
                .cancelledOrders(statusCounts.getOrDefault(OrderStatus.CANCELLED, 0L))
                .overdueOrders(orderRepository.findOverdueOrders(LocalDateTime.now()).size())
                .totalRevenue(totalRevenue)
                .todaysRevenue(todaysRevenue)
                .averageOrderValue(averageOrderValue)
                .ordersByStatus(ordersByStatus)
                .conversionRate(conversionRate)
                .build();
    }

    @Override
    public String generateOrderNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = String.format("%04d", new Random().nextInt(10000));
        String orderNumber = "LV-" + datePrefix + "-" + randomSuffix;
        
        // Проверяем уникальность номера
        while (orderRepository.existsByOrderNumber(orderNumber)) {
            randomSuffix = String.format("%04d", new Random().nextInt(10000));
            orderNumber = "LV-" + datePrefix + "-" + randomSuffix;
        }
        
        return orderNumber;
    }

    @Override
    public boolean canChangeOrderStatus(OrderStatus currentStatus, OrderStatus newStatus) {
        // Логика валидации переходов между статусами
        switch (currentStatus) {
            case NEW:
                return newStatus == OrderStatus.CONFIRMED || 
                       newStatus == OrderStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == OrderStatus.IN_PROGRESS || 
                       newStatus == OrderStatus.CANCELLED;
            case IN_PROGRESS:
                return newStatus == OrderStatus.READY || 
                       newStatus == OrderStatus.CANCELLED;
            case READY:
                return newStatus == OrderStatus.OUT_FOR_DELIVERY || 
                       newStatus == OrderStatus.DELIVERED ||
                       newStatus == OrderStatus.RETURNED;
            case OUT_FOR_DELIVERY:
                return newStatus == OrderStatus.DELIVERED || 
                       newStatus == OrderStatus.RETURNED;
            case DELIVERED:
                return false; // Доставленный заказ нельзя изменить
            case CANCELLED:
                return false; // Отмененный заказ нельзя изменить
            case RETURNED:
                return newStatus == OrderStatus.CANCELLED;
            default:
                return false;
        }
    }

    @Override
    @Transactional
    public Order recalculateOrderAmount(Long orderId) {
        log.info("Пересчет стоимости заказа {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден: " + orderId));
        
        order.calculateTotalAmount();
        Order updatedOrder = orderRepository.save(order);
        
        log.info("Стоимость заказа {} пересчитана: {}", 
                order.getOrderNumber(), updatedOrder.getFinalAmount());
        
        return updatedOrder;
    }
}
