package com.omstu.lavanda.floristic.controller;

import com.omstu.lavanda.floristic.dto.FreshnessCalendarDto;
import com.omstu.lavanda.floristic.mapper.FreshnessCalendarMapper;
import com.omstu.lavanda.floristic.model.FreshnessCalendar;
import com.omstu.lavanda.floristic.model.FreshnessStatus;
import com.omstu.lavanda.floristic.service.FreshnessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST контроллер для управления календарем свежести в флористическом учете LAVANDA ERP
 */
@RestController
@RequestMapping("/api/floristic/freshness")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FreshnessCalendarController {

    private final FreshnessService freshnessService;
    private final FreshnessCalendarMapper freshnessMapper;

    /**
     * Получить запись календаря свежести по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FreshnessCalendarDto> getFreshnessRecordById(@PathVariable Long id) {
        log.info("Запрос записи календаря свежести по ID: {}", id);
        
        return freshnessService.getFreshnessRecordById(id)
                .map(freshnessMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получить записи по статусу свежести
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FreshnessCalendarDto>> getRecordsByStatus(@PathVariable FreshnessStatus status) {
        log.info("Получение записей по статусу свежести: {}", status);
        
        List<FreshnessCalendar> records = freshnessService.getRecordsByFreshnessStatus(status);
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить записи, истекающие сегодня
     */
    @GetMapping("/expiring-today")
    public ResponseEntity<List<FreshnessCalendarDto>> getRecordsExpiringToday() {
        log.info("Получение записей, истекающих сегодня");
        
        List<FreshnessCalendar> records = freshnessService.getRecordsExpiringToday();
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить записи, истекающие до определенной даты
     */
    @GetMapping("/expiring-before")
    public ResponseEntity<List<FreshnessCalendarDto>> getRecordsExpiringBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        log.info("Получение записей, истекающих до: {}", date);
        
        List<FreshnessCalendar> records = freshnessService.getRecordsExpiringBefore(date);
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить просроченные записи
     */
    @GetMapping("/expired")
    public ResponseEntity<List<FreshnessCalendarDto>> getExpiredRecords() {
        log.info("Получение просроченных записей");
        
        List<FreshnessCalendar> records = freshnessService.getExpiredRecords();
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить записи, требующие скидки
     */
    @GetMapping("/needing-discount")
    public ResponseEntity<List<FreshnessCalendarDto>> getRecordsNeedingDiscount() {
        log.info("Получение записей, требующих скидки");
        
        List<FreshnessCalendar> records = freshnessService.getRecordsNeedingDiscount();
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить рекомендации по скидкам
     */
    @GetMapping("/discount-recommendations")
    public ResponseEntity<List<FreshnessCalendarDto>> getDiscountRecommendations() {
        log.info("Получение рекомендаций по скидкам");
        
        List<FreshnessCalendar> records = freshnessService.getDiscountRecommendations();
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить записи по номеру партии
     */
    @GetMapping("/batch/{batchNumber}")
    public ResponseEntity<List<FreshnessCalendarDto>> getRecordsByBatchNumber(@PathVariable String batchNumber) {
        log.info("Получение записей по номеру партии: {}", batchNumber);
        
        List<FreshnessCalendar> records = freshnessService.getRecordsByBatchNumber(batchNumber);
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить записи по дате поступления
     */
    @GetMapping("/delivery-date")
    public ResponseEntity<List<FreshnessCalendarDto>> getRecordsByDeliveryDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate) {
        
        log.info("Получение записей по дате поступления: {}", deliveryDate);
        
        List<FreshnessCalendar> records = freshnessService.getRecordsByDeliveryDate(deliveryDate);
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Получить проданные записи в периоде
     */
    @GetMapping("/sold-in-period")
    public ResponseEntity<List<FreshnessCalendarDto>> getSoldRecordsInPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        log.info("Получение проданных записей в периоде: {} - {}", startDate, endDate);
        
        List<FreshnessCalendar> records = freshnessService.getSoldRecordsInPeriod(startDate, endDate);
        List<FreshnessCalendarDto> recordDtos = records.stream()
                .map(freshnessMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recordDtos);
    }

    /**
     * Отметить запись как проданную
     */
    @PatchMapping("/{id}/mark-sold")
    public ResponseEntity<FreshnessCalendarDto> markAsSold(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate soldDate) {
        
        LocalDate actualSoldDate = soldDate != null ? soldDate : LocalDate.now();
        log.info("Отметка записи {} как проданной на дату: {}", id, actualSoldDate);
        
        try {
            FreshnessCalendar updatedRecord = freshnessService.markAsSold(id, actualSoldDate);
            return ResponseEntity.ok(freshnessMapper.toDto(updatedRecord));
        } catch (RuntimeException e) {
            log.error("Ошибка при отметке записи как проданной: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Применить скидку к записи
     */
    @PatchMapping("/{id}/apply-discount")
    public ResponseEntity<FreshnessCalendarDto> applyDiscount(
            @PathVariable Long id,
            @RequestParam Integer discountPercentage) {
        
        log.info("Применение скидки {}% к записи: {}", discountPercentage, id);
        
        try {
            FreshnessCalendar updatedRecord = freshnessService.applyDiscount(id, discountPercentage);
            return ResponseEntity.ok(freshnessMapper.toDto(updatedRecord));
        } catch (RuntimeException e) {
            log.error("Ошибка при применении скидки: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Обновить статусы свежести для всех записей
     */
    @PostMapping("/update-all-statuses")
    public ResponseEntity<Void> updateAllFreshnessStatuses() {
        log.info("Обновление статусов свежести для всех записей");
        
        freshnessService.updateAllFreshnessStatuses();
        return ResponseEntity.ok().build();
    }

    /**
     * Получить статистику по статусам свежести
     */
    @GetMapping("/statistics")
    public ResponseEntity<List<Object[]>> getFreshnessStatistics() {
        log.info("Получение статистики по статусам свежести");
        
        List<Object[]> statistics = freshnessService.getFreshnessStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Очистить просроченные записи
     */
    @DeleteMapping("/cleanup-expired")
    public ResponseEntity<Void> cleanupExpiredRecords(
            @RequestParam(defaultValue = "30") int daysOld) {
        
        log.info("Очистка просроченных записей старше {} дней", daysOld);
        
        freshnessService.cleanupExpiredRecords(daysOld);
        return ResponseEntity.ok().build();
    }
}
