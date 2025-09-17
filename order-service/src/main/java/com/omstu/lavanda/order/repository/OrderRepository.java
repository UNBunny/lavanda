package com.omstu.lavanda.order.repository;

import com.omstu.lavanda.order.model.Order;
import com.omstu.lavanda.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с заказами
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Найти заказ по номеру
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Найти заказы по статусу
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Найти заказы по статусу с пагинацией
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Найти заказы клиента по телефону
     */
    List<Order> findByCustomerPhoneOrderByCreatedAtDesc(String customerPhone);

    /**
     * Найти заказы клиента по email
     */
    List<Order> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);

    /**
     * Найти заказы назначенного флориста
     */
    List<Order> findByAssignedFloristId(Long floristId);

    /**
     * Найти заказы за период
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Найти заказы с доставкой на определенную дату
     */
    @Query("SELECT o FROM Order o WHERE DATE(o.deliveryDate) = DATE(:deliveryDate)")
    List<Order> findOrdersByDeliveryDate(@Param("deliveryDate") LocalDateTime deliveryDate);

    /**
     * Найти заказы, требующие обработки (новые и подтвержденные)
     */
    @Query("SELECT o FROM Order o WHERE o.status IN (com.omstu.lavanda.order.model.OrderStatus.NEW, com.omstu.lavanda.order.model.OrderStatus.CONFIRMED) ORDER BY o.createdAt ASC")
    List<Order> findOrdersRequiringProcessing();

    /**
     * Найти заказы в работе у флориста
     */
    @Query("SELECT o FROM Order o WHERE o.status = com.omstu.lavanda.order.model.OrderStatus.IN_PROGRESS AND o.assignedFloristId = :floristId")
    List<Order> findActiveOrdersByFlorist(@Param("floristId") Long floristId);

    /**
     * Найти заказы готовые к доставке
     */
    @Query("SELECT o FROM Order o WHERE o.status = com.omstu.lavanda.order.model.OrderStatus.READY AND o.deliveryAddress IS NOT NULL")
    List<Order> findOrdersReadyForDelivery();

    /**
     * Найти просроченные заказы (не доставлены в срок)
     */
    @Query("SELECT o FROM Order o WHERE o.deliveryDate < :currentTime AND o.status NOT IN (com.omstu.lavanda.order.model.OrderStatus.DELIVERED, com.omstu.lavanda.order.model.OrderStatus.CANCELLED, com.omstu.lavanda.order.model.OrderStatus.RETURNED)")
    List<Order> findOverdueOrders(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Подсчитать заказы по статусу
     */
    long countByStatus(OrderStatus status);

    /**
     * Подсчитать заказы за сегодня
     */
    @Query(value = "SELECT COUNT(*) FROM orders WHERE DATE(created_at) = CURRENT_DATE", nativeQuery = true)
    long countTodaysOrders();

    /**
     * Получить статистику заказов за период
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate GROUP BY o.status")
    List<Object[]> getOrderStatisticsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    /**
     * Найти заказы с поиском по имени клиента или номеру заказа
     */
    @Query("SELECT o FROM Order o WHERE " +
            "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Order> searchOrders(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Проверить существование заказа с данным номером
     */
    boolean existsByOrderNumber(String orderNumber);
}