package com.omstu.lavanda.floristic.service.impl;

import com.omstu.lavanda.floristic.model.Flower;
import com.omstu.lavanda.floristic.model.FlowerColor;
import com.omstu.lavanda.floristic.model.FlowerType;
import com.omstu.lavanda.floristic.model.SeasonalAvailability;
import com.omstu.lavanda.floristic.repository.FlowerRepository;
import com.omstu.lavanda.floristic.service.FlowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с цветами в флористическом учете LAVANDA ERP
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowerServiceImpl implements FlowerService {

    private final FlowerRepository flowerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getAllActiveFlowers() {
        log.debug("Получение всех активных цветов");
        return flowerRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Flower> getFlowerById(Long id) {
        log.debug("Поиск цветка по ID: {}", id);
        return flowerRepository.findById(id);
    }

    @Override
    public Flower createFlower(Flower flower) {
        log.info("Создание нового цветка: {} {}", flower.getType(), flower.getName());
        
        // Устанавливаем значения по умолчанию
        if (flower.getIsActive() == null) {
            flower.setIsActive(true);
        }
        if (flower.getReservedStock() == null) {
            flower.setReservedStock(0);
        }
        
        // Вычисляем дату истечения свежести
        if (flower.getDeliveryDate() != null && flower.getFreshnessDays() != null) {
            flower.setExpiryDate(flower.getDeliveryDate().plusDays(flower.getFreshnessDays()));
        }
        
        Flower savedFlower = flowerRepository.save(flower);
        log.info("Цветок создан с ID: {}", savedFlower.getId());
        return savedFlower;
    }

    @Override
    public Flower updateFlower(Long id, Flower flower) {
        log.info("Обновление цветка с ID: {}", id);
        
        Flower existingFlower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Цветок не найден с ID: " + id));
        
        // Обновляем поля
        existingFlower.setName(flower.getName());
        existingFlower.setVariety(flower.getVariety());
        existingFlower.setType(flower.getType());
        existingFlower.setColor(flower.getColor());
        existingFlower.setMinStockLevel(flower.getMinStockLevel());
        existingFlower.setPurchasePrice(flower.getPurchasePrice());
        existingFlower.setSellingPrice(flower.getSellingPrice());
        existingFlower.setSupplier(flower.getSupplier());
        existingFlower.setCountryOrigin(flower.getCountryOrigin());
        existingFlower.setStemLength(flower.getStemLength());
        existingFlower.setFreshnessDays(flower.getFreshnessDays());
        existingFlower.setSeasonalAvailability(flower.getSeasonalAvailability());
        existingFlower.setNotes(flower.getNotes());
        
        // Пересчитываем дату истечения если изменились связанные поля
        if (existingFlower.getDeliveryDate() != null && existingFlower.getFreshnessDays() != null) {
            existingFlower.setExpiryDate(existingFlower.getDeliveryDate().plusDays(existingFlower.getFreshnessDays()));
        }
        
        return flowerRepository.save(existingFlower);
    }

    @Override
    public void deactivateFlower(Long id) {
        log.info("Деактивация цветка с ID: {}", id);
        
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Цветок не найден с ID: " + id));
        
        flower.setIsActive(false);
        flowerRepository.save(flower);
        
        log.info("Цветок деактивирован: {}", flower.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersByType(FlowerType type) {
        log.debug("Поиск цветов по типу: {}", type);
        return flowerRepository.findByTypeAndIsActiveTrue(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersByColor(FlowerColor color) {
        log.debug("Поиск цветов по цвету: {}", color);
        return flowerRepository.findByColorAndIsActiveTrue(color);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersByTypeAndColor(FlowerType type, FlowerColor color) {
        log.debug("Поиск цветов по типу {} и цвету {}", type, color);
        return flowerRepository.findByTypeAndColorAndIsActiveTrue(type, color);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersNeedingRestock() {
        log.debug("Поиск цветов, требующих пополнения");
        return flowerRepository.findFlowersNeedingRestock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersExpiringBefore(LocalDate date) {
        log.debug("Поиск цветов, истекающих до: {}", date);
        return flowerRepository.findFlowersExpiringBefore(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFreshFlowers() {
        log.debug("Поиск свежих цветов");
        return flowerRepository.findFreshFlowers();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersBySeasonalAvailability(SeasonalAvailability seasonalAvailability) {
        log.debug("Поиск цветов по сезонной доступности: {}", seasonalAvailability);
        return flowerRepository.findBySeasonalAvailabilityAndIsActiveTrue(seasonalAvailability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> searchFlowers(String searchTerm) {
        log.debug("Поиск цветов по запросу: {}", searchTerm);
        return flowerRepository.searchByNameOrVariety(searchTerm);
    }

    @Override
    public boolean reserveFlowers(Long flowerId, Integer quantity) {
        log.info("Резервирование {} штук цветка с ID: {}", quantity, flowerId);
        
        Flower flower = flowerRepository.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Цветок не найден с ID: " + flowerId));
        
        if (flower.getAvailableStock() < quantity) {
            log.warn("Недостаточно цветов для резервирования. Доступно: {}, запрошено: {}", 
                    flower.getAvailableStock(), quantity);
            return false;
        }
        
        flower.setReservedStock(flower.getReservedStock() + quantity);
        flowerRepository.save(flower);
        
        log.info("Зарезервировано {} штук цветка: {}", quantity, flower.getName());
        return true;
    }

    @Override
    public boolean releaseReservation(Long flowerId, Integer quantity) {
        log.info("Освобождение резерва {} штук цветка с ID: {}", quantity, flowerId);
        
        Flower flower = flowerRepository.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Цветок не найден с ID: " + flowerId));
        
        if (flower.getReservedStock() < quantity) {
            log.warn("Попытка освободить больше резерва чем есть. Зарезервировано: {}, запрошено к освобождению: {}", 
                    flower.getReservedStock(), quantity);
            return false;
        }
        
        flower.setReservedStock(flower.getReservedStock() - quantity);
        flowerRepository.save(flower);
        
        log.info("Освобожден резерв {} штук цветка: {}", quantity, flower.getName());
        return true;
    }

    @Override
    public Flower updateStock(Long flowerId, Integer quantityChange, String reason) {
        log.info("Обновление остатка цветка с ID: {} на {} штук. Причина: {}", 
                flowerId, quantityChange, reason);
        
        Flower flower = flowerRepository.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Цветок не найден с ID: " + flowerId));
        
        int newStock = flower.getCurrentStock() + quantityChange;
        if (newStock < 0) {
            throw new RuntimeException("Остаток не может быть отрицательным");
        }
        
        flower.setCurrentStock(newStock);
        
        // Если это поступление, обновляем дату поступления и пересчитываем срок годности
        if (quantityChange > 0) {
            flower.setDeliveryDate(LocalDate.now());
            if (flower.getFreshnessDays() != null) {
                flower.setExpiryDate(LocalDate.now().plusDays(flower.getFreshnessDays()));
            }
        }
        
        Flower savedFlower = flowerRepository.save(flower);
        log.info("Остаток цветка {} обновлен. Новый остаток: {}", flower.getName(), newStock);
        
        return savedFlower;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getFlowerStatisticsByType() {
        log.debug("Получение статистики по типам цветов");
        return flowerRepository.getFlowerStatisticsByType();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalStockValue() {
        log.debug("Получение общей стоимости остатков цветов");
        return flowerRepository.getTotalStockValue();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(Long flowerId, Integer requiredQuantity) {
        log.debug("Проверка доступности {} штук цветка с ID: {}", requiredQuantity, flowerId);
        
        Flower flower = flowerRepository.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Цветок не найден с ID: " + flowerId));
        
        return flower.getAvailableStock() >= requiredQuantity && flower.isFresh();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersExpiringToday() {
        log.debug("Поиск цветов, истекающих сегодня");
        return flowerRepository.findFlowersExpiringBefore(LocalDate.now().plusDays(1));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersBySupplier(String supplier) {
        log.debug("Поиск цветов по поставщику: {}", supplier);
        return flowerRepository.findBySupplierContainingIgnoreCaseAndIsActiveTrue(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flower> getFlowersByCountryOrigin(String countryOrigin) {
        log.debug("Поиск цветов по стране происхождения: {}", countryOrigin);
        return flowerRepository.findByCountryOriginContainingIgnoreCaseAndIsActiveTrue(countryOrigin);
    }
}
