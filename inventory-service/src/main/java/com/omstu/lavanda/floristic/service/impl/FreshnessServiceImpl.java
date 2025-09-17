package com.omstu.lavanda.floristic.service.impl;

import com.omstu.lavanda.floristic.model.Flower;
import com.omstu.lavanda.floristic.model.FreshnessCalendar;
import com.omstu.lavanda.floristic.model.FreshnessStatus;
import com.omstu.lavanda.floristic.repository.FreshnessCalendarRepository;
import com.omstu.lavanda.floristic.service.FreshnessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с календарем свежести цветов в флористическом учете LAVANDA ERP
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FreshnessServiceImpl implements FreshnessService {

    private final FreshnessCalendarRepository freshnessCalendarRepository;

    @Override
    public FreshnessCalendar createFreshnessRecord(FreshnessCalendar freshnessCalendar) {
        log.info("Создание записи календаря свежести для цветка: {}", 
                freshnessCalendar.getFlower().getName());
        
        // Устанавливаем значения по умолчанию
        if (freshnessCalendar.getIsSold() == null) {
            freshnessCalendar.setIsSold(false);
        }
        
        FreshnessCalendar savedRecord = freshnessCalendarRepository.save(freshnessCalendar);
        log.info("Запись календаря свежести создана с ID: {}", savedRecord.getId());
        return savedRecord;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FreshnessCalendar> getFreshnessRecordById(Long id) {
        log.debug("Поиск записи календаря свежести по ID: {}", id);
        return freshnessCalendarRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getFreshnessRecordsByFlower(Flower flower) {
        log.debug("Поиск записей календаря свежести для цветка: {}", flower.getName());
        return freshnessCalendarRepository.findByFlowerAndIsSoldFalse(flower);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getRecordsByFreshnessStatus(FreshnessStatus status) {
        log.debug("Поиск записей по статусу свежести: {}", status);
        return freshnessCalendarRepository.findByFreshnessStatusAndIsSoldFalse(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getRecordsExpiringToday() {
        log.debug("Поиск записей, истекающих сегодня");
        return freshnessCalendarRepository.findExpiringToday();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getRecordsExpiringBefore(LocalDate date) {
        log.debug("Поиск записей, истекающих до: {}", date);
        return freshnessCalendarRepository.findExpiringBefore(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getExpiredRecords() {
        log.debug("Поиск просроченных записей");
        return freshnessCalendarRepository.findExpired();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getRecordsNeedingDiscount() {
        log.debug("Поиск записей, требующих скидки");
        return freshnessCalendarRepository.findNeedingDiscount();
    }

    @Override
    public void updateAllFreshnessStatuses() {
        log.info("Обновление статусов свежести для всех записей");
        
        List<FreshnessCalendar> allRecords = freshnessCalendarRepository.findAll();
        int updatedCount = 0;
        
        for (FreshnessCalendar record : allRecords) {
            if (!record.getIsSold()) {
                FreshnessStatus oldStatus = record.getFreshnessStatus();
                record.updateDaysUntilExpiry(); // Это обновит и статус свежести
                
                if (!oldStatus.equals(record.getFreshnessStatus())) {
                    freshnessCalendarRepository.save(record);
                    updatedCount++;
                }
            }
        }
        
        log.info("Обновлено статусов свежести: {}", updatedCount);
    }

    @Override
    public FreshnessCalendar markAsSold(Long recordId, LocalDate soldDate) {
        log.info("Отметка записи {} как проданной на дату: {}", recordId, soldDate);
        
        FreshnessCalendar record = freshnessCalendarRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Запись календаря свежести не найдена с ID: " + recordId));
        
        record.setIsSold(true);
        record.setSoldDate(soldDate);
        
        FreshnessCalendar savedRecord = freshnessCalendarRepository.save(record);
        log.info("Запись отмечена как проданная: {}", recordId);
        
        return savedRecord;
    }

    @Override
    public FreshnessCalendar applyDiscount(Long recordId, Integer discountPercentage) {
        log.info("Применение скидки {}% к записи: {}", discountPercentage, recordId);
        
        FreshnessCalendar record = freshnessCalendarRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Запись календаря свежести не найдена с ID: " + recordId));
        
        record.setDiscountPercentage(discountPercentage);
        
        FreshnessCalendar savedRecord = freshnessCalendarRepository.save(record);
        log.info("Скидка {}% применена к записи: {}", discountPercentage, recordId);
        
        return savedRecord;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getFreshnessStatistics() {
        log.debug("Получение статистики по статусам свежести");
        return freshnessCalendarRepository.getFreshnessStatistics();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getRecordsByBatchNumber(String batchNumber) {
        log.debug("Поиск записей по номеру партии: {}", batchNumber);
        return freshnessCalendarRepository.findByBatchNumberAndIsSoldFalse(batchNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getRecordsByDeliveryDate(LocalDate deliveryDate) {
        log.debug("Поиск записей по дате поступления: {}", deliveryDate);
        return freshnessCalendarRepository.findByDeliveryDateAndIsSoldFalse(deliveryDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getSoldRecordsInPeriod(LocalDate startDate, LocalDate endDate) {
        log.debug("Поиск проданных записей в периоде: {} - {}", startDate, endDate);
        return freshnessCalendarRepository.findSoldInPeriod(startDate, endDate);
    }

    @Override
    public FreshnessCalendar createAutomaticFreshnessRecord(Flower flower, Integer quantity, String batchNumber) {
        log.info("Создание автоматической записи свежести для цветка: {}, количество: {}, партия: {}", 
                flower.getName(), quantity, batchNumber);
        
        FreshnessCalendar record = FreshnessCalendar.builder()
                .flower(flower)
                .batchNumber(batchNumber)
                .quantity(quantity)
                .deliveryDate(LocalDate.now())
                .expiryDate(flower.getExpiryDate())
                .freshnessStatus(FreshnessStatus.FRESH)
                .isSold(false)
                .build();
        
        // Устанавливаем условия хранения по умолчанию для цветов
        record.setStorageConditions("Холодильник +2°C, влажность 85%");
        record.setTemperatureCelsius(2);
        record.setHumidityPercentage(85);
        
        return createFreshnessRecord(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreshnessCalendar> getDiscountRecommendations() {
        log.debug("Получение рекомендаций по скидкам");
        List<FreshnessCalendar> needingDiscount = freshnessCalendarRepository.findNeedingDiscount();
        
        // Применяем рекомендуемые скидки
        for (FreshnessCalendar record : needingDiscount) {
            if (record.getDiscountPercentage() == null || record.getDiscountPercentage() == 0) {
                Integer recommendedDiscount = record.getRecommendedDiscount();
                if (recommendedDiscount > 0) {
                    record.setDiscountPercentage(recommendedDiscount);
                }
            }
        }
        
        return needingDiscount;
    }

    @Override
    public void cleanupExpiredRecords(int daysOld) {
        log.info("Очистка просроченных записей старше {} дней", daysOld);
        
        LocalDate cutoffDate = LocalDate.now().minusDays(daysOld);
        List<FreshnessCalendar> expiredRecords = freshnessCalendarRepository.findExpired();
        
        int deletedCount = 0;
        for (FreshnessCalendar record : expiredRecords) {
            if (record.getExpiryDate().isBefore(cutoffDate)) {
                freshnessCalendarRepository.delete(record);
                deletedCount++;
            }
        }
        
        log.info("Удалено просроченных записей: {}", deletedCount);
    }
}
