package com.omstu.lavanda.floristic.repository;

import com.omstu.lavanda.floristic.model.Flower;
import com.omstu.lavanda.floristic.model.FreshnessCalendar;
import com.omstu.lavanda.floristic.model.FreshnessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с календарем свежести в флористическом учете LAVANDA ERP
 */
@Repository
public interface FreshnessCalendarRepository extends JpaRepository<FreshnessCalendar, Long> {

    /**
     * Найти записи календаря по цветку
     */
    List<FreshnessCalendar> findByFlowerAndIsSoldFalse(Flower flower);

    /**
     * Найти записи по статусу свежести
     */
    List<FreshnessCalendar> findByFreshnessStatusAndIsSoldFalse(FreshnessStatus status);

    /**
     * Найти записи, истекающие сегодня
     */
    @Query("SELECT fc FROM FreshnessCalendar fc WHERE fc.isSold = false AND " +
           "fc.expiryDate = CURRENT_DATE")
    List<FreshnessCalendar> findExpiringToday();

    /**
     * Найти записи, истекающие в ближайшие дни
     */
    @Query("SELECT fc FROM FreshnessCalendar fc WHERE fc.isSold = false AND " +
           "fc.expiryDate BETWEEN CURRENT_DATE AND :date")
    List<FreshnessCalendar> findExpiringBefore(@Param("date") LocalDate date);

    /**
     * Найти просроченные записи
     */
    @Query("SELECT fc FROM FreshnessCalendar fc WHERE fc.isSold = false AND " +
           "fc.expiryDate < CURRENT_DATE")
    List<FreshnessCalendar> findExpired();

    /**
     * Найти записи, требующие скидки
     */
    @Query("SELECT fc FROM FreshnessCalendar fc WHERE fc.isSold = false AND " +
           "fc.freshnessStatus IN ('WARNING', 'CRITICAL', 'EXPIRES_TODAY')")
    List<FreshnessCalendar> findNeedingDiscount();

    /**
     * Найти записи по номеру партии
     */
    List<FreshnessCalendar> findByBatchNumberAndIsSoldFalse(String batchNumber);

    /**
     * Найти записи по дате поступления
     */
    List<FreshnessCalendar> findByDeliveryDateAndIsSoldFalse(LocalDate deliveryDate);

    /**
     * Получить статистику по статусам свежести
     */
    @Query("SELECT fc.freshnessStatus, COUNT(fc), SUM(fc.quantity) " +
           "FROM FreshnessCalendar fc WHERE fc.isSold = false GROUP BY fc.freshnessStatus")
    List<Object[]> getFreshnessStatistics();

    /**
     * Подсчитать общее количество цветов по статусу
     */
    @Query("SELECT SUM(fc.quantity) FROM FreshnessCalendar fc " +
           "WHERE fc.isSold = false AND fc.freshnessStatus = :status")
    Long getTotalQuantityByStatus(@Param("status") FreshnessStatus status);

    /**
     * Найти записи с определенным процентом скидки
     */
    List<FreshnessCalendar> findByDiscountPercentageGreaterThanAndIsSoldFalse(Integer discountPercentage);

    /**
     * Найти записи по диапазону дат истечения
     */
    @Query("SELECT fc FROM FreshnessCalendar fc WHERE fc.isSold = false AND " +
           "fc.expiryDate BETWEEN :startDate AND :endDate")
    List<FreshnessCalendar> findByExpiryDateRange(@Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);

    /**
     * Найти записи по условиям хранения
     */
    List<FreshnessCalendar> findByStorageConditionsContainingIgnoreCaseAndIsSoldFalse(String conditions);

    /**
     * Получить записи, проданные в определенный период
     */
    @Query("SELECT fc FROM FreshnessCalendar fc WHERE fc.isSold = true AND " +
           "fc.soldDate BETWEEN :startDate AND :endDate")
    List<FreshnessCalendar> findSoldInPeriod(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
}
