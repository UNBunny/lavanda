package com.omstu.lavanda.floristic.service;

import com.omstu.lavanda.floristic.model.Flower;
import com.omstu.lavanda.floristic.model.FreshnessCalendar;
import com.omstu.lavanda.floristic.model.FreshnessStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с календарем свежести цветов в флористическом учете LAVANDA ERP
 */
public interface FreshnessService {

    /**
     * Создать запись в календаре свежести
     */
    FreshnessCalendar createFreshnessRecord(FreshnessCalendar freshnessCalendar);

    /**
     * Получить запись по ID
     */
    Optional<FreshnessCalendar> getFreshnessRecordById(Long id);

    /**
     * Получить все записи для цветка
     */
    List<FreshnessCalendar> getFreshnessRecordsByFlower(Flower flower);

    /**
     * Получить записи по статусу свежести
     */
    List<FreshnessCalendar> getRecordsByFreshnessStatus(FreshnessStatus status);

    /**
     * Получить записи, истекающие сегодня
     */
    List<FreshnessCalendar> getRecordsExpiringToday();

    /**
     * Получить записи, истекающие в ближайшие дни
     */
    List<FreshnessCalendar> getRecordsExpiringBefore(LocalDate date);

    /**
     * Получить просроченные записи
     */
    List<FreshnessCalendar> getExpiredRecords();

    /**
     * Получить записи, требующие скидки
     */
    List<FreshnessCalendar> getRecordsNeedingDiscount();

    /**
     * Обновить статус свежести для всех записей
     */
    void updateAllFreshnessStatuses();

    /**
     * Отметить запись как проданную
     */
    FreshnessCalendar markAsSold(Long recordId, LocalDate soldDate);

    /**
     * Применить скидку к записи
     */
    FreshnessCalendar applyDiscount(Long recordId, Integer discountPercentage);

    /**
     * Получить статистику по статусам свежести
     */
    List<Object[]> getFreshnessStatistics();

    /**
     * Получить записи по номеру партии
     */
    List<FreshnessCalendar> getRecordsByBatchNumber(String batchNumber);

    /**
     * Получить записи по дате поступления
     */
    List<FreshnessCalendar> getRecordsByDeliveryDate(LocalDate deliveryDate);

    /**
     * Получить записи, проданные в определенный период
     */
    List<FreshnessCalendar> getSoldRecordsInPeriod(LocalDate startDate, LocalDate endDate);

    /**
     * Создать автоматические записи свежести для нового поступления цветов
     */
    FreshnessCalendar createAutomaticFreshnessRecord(Flower flower, Integer quantity, String batchNumber);

    /**
     * Получить рекомендации по скидкам для истекающих цветов
     */
    List<FreshnessCalendar> getDiscountRecommendations();

    /**
     * Удалить просроченные записи (старше определенного периода)
     */
    void cleanupExpiredRecords(int daysOld);
}
