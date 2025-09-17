package com.omstu.lavanda.order.repository;

import com.omstu.lavanda.order.model.OrderItem;
import com.omstu.lavanda.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с позициями заказов
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Найти все позиции заказа
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Найти позиции по ID товара
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * Найти позиции букета/композиции
     */
    List<OrderItem> findByParentBouquetId(Long parentBouquetId);

    /**
     * Найти компоненты букетов
     */
    List<OrderItem> findByIsBouquetComponentTrue();

    /**
     * Получить статистику продаж товара за период
     */
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi JOIN oi.order o " +
            "WHERE oi.productId = :productId AND o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.status NOT IN (com.omstu.lavanda.order.model.OrderStatus.CANCELLED, com.omstu.lavanda.order.model.OrderStatus.RETURNED)")
    Double getTotalQuantitySoldByProductAndDateRange(@Param("productId") Long productId,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Получить топ продаваемых товаров
     */
    @Query("SELECT oi.productId, oi.productName, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderItem oi JOIN oi.order o " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.status NOT IN (com.omstu.lavanda.order.model.OrderStatus.CANCELLED, com.omstu.lavanda.order.model.OrderStatus.RETURNED) " +
            "GROUP BY oi.productId, oi.productName " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> getTopSellingProducts(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Получить общую выручку по товару за период
     */
    @Query("SELECT SUM(oi.totalPrice) FROM OrderItem oi JOIN oi.order o " +
            "WHERE oi.productId = :productId AND o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.status NOT IN (com.omstu.lavanda.order.model.OrderStatus.CANCELLED, com.omstu.lavanda.order.model.OrderStatus.RETURNED)")
    Double getTotalRevenueByProductAndDateRange(@Param("productId") Long productId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    /**
     * Найти позиции заказов по типу товара
     */
    List<OrderItem> findByProductType(String productType);

    /**
     * Подсчитать количество использований товара в заказах
     */
    @Query("SELECT COUNT(oi) FROM OrderItem oi JOIN oi.order o " +
            "WHERE oi.productId = :productId AND o.status NOT IN (com.omstu.lavanda.order.model.OrderStatus.CANCELLED, com.omstu.lavanda.order.model.OrderStatus.RETURNED)")
    long countUsageByProduct(@Param("productId") Long productId);
}