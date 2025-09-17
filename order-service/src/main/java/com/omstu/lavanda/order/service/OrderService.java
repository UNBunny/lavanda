package com.omstu.lavanda.order.service;

import com.omstu.lavanda.order.model.Order;
import com.omstu.lavanda.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления заказами в цветочной мастерской LAVANDA
 */
public interface OrderService {

    /**
     * Создать новый заказ
     */
    Order createOrder(Order order);

    /**
     * Получить заказ по ID
     */
    Optional<Order> getOrderById(Long id);

    /**
     * Получить заказ по номеру
     */
    Optional<Order> getOrderByNumber(String orderNumber);

    /**
     * Получить все заказы с пагинацией
     */
    Page<Order> getAllOrders(Pageable pageable);

    /**
     * Получить заказы по статусу
     */
    List<Order> getOrdersByStatus(OrderStatus status);

    /**
     * Получить заказы клиента по телефону
     */
    List<Order> getOrdersByCustomerPhone(String customerPhone);

    /**
     * Получить заказы клиента по email
     */
    List<Order> getOrdersByCustomerEmail(String customerEmail);

    /**
     * Обновить заказ
     */
    Order updateOrder(Order order);

    /**
     * Изменить статус заказа
     */
    Order updateOrderStatus(Long orderId, OrderStatus newStatus);

    /**
     * Назначить флориста на заказ
     */
    Order assignFloristToOrder(Long orderId, Long floristId);

    /**
     * Отменить заказ
     */
    Order cancelOrder(Long orderId, String reason);

    /**
     * Удалить заказ
     */
    void deleteOrder(Long orderId);

    /**
     * Найти заказы за период
     */
    List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Найти заказы с доставкой на определенную дату
     */
    List<Order> getOrdersByDeliveryDate(LocalDateTime deliveryDate);

    /**
     * Получить заказы, требующие обработки
     */
    List<Order> getOrdersRequiringProcessing();

    /**
     * Получить активные заказы флориста
     */
    List<Order> getActiveOrdersByFlorist(Long floristId);

    /**
     * Получить заказы готовые к доставке
     */
    List<Order> getOrdersReadyForDelivery();

    /**
     * Получить просроченные заказы
     */
    List<Order> getOverdueOrders();

    /**
     * Поиск заказов по критериям
     */
    Page<Order> searchOrders(String searchTerm, Pageable pageable);

    /**
     * Получить статистику заказов
     */
    OrderStatistics getOrderStatistics();

    /**
     * Получить статистику заказов за период
     */
    OrderStatistics getOrderStatisticsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Сгенерировать уникальный номер заказа
     */
    String generateOrderNumber();

    /**
     * Проверить возможность изменения статуса заказа
     */
    boolean canChangeOrderStatus(OrderStatus currentStatus, OrderStatus newStatus);

    /**
     * Пересчитать стоимость заказа
     */
    Order recalculateOrderAmount(Long orderId);
}
