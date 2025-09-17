package com.omstu.lavanda.floristic.service.impl;

import com.omstu.lavanda.floristic.model.Material;
import com.omstu.lavanda.floristic.model.MaterialType;
import com.omstu.lavanda.floristic.repository.MaterialRepository;
import com.omstu.lavanda.floristic.service.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с материалами в флористическом учете LAVANDA ERP
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Material> getAllActiveMaterials() {
        log.debug("Получение всех активных материалов");
        return materialRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Material> getMaterialById(Long id) {
        log.debug("Поиск материала по ID: {}", id);
        return materialRepository.findById(id);
    }

    @Override
    public Material createMaterial(Material material) {
        log.info("Создание нового материала: {} {}", material.getType(), material.getName());
        
        // Устанавливаем значения по умолчанию
        if (material.getIsActive() == null) {
            material.setIsActive(true);
        }
        if (material.getReservedStockMeters() == null) {
            material.setReservedStockMeters(BigDecimal.ZERO);
        }
        if (material.getIsWaterproof() == null) {
            material.setIsWaterproof(false);
        }
        
        Material savedMaterial = materialRepository.save(material);
        log.info("Материал создан с ID: {}", savedMaterial.getId());
        return savedMaterial;
    }

    @Override
    public Material updateMaterial(Long id, Material material) {
        log.info("Обновление материала с ID: {}", id);
        
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Материал не найден с ID: " + id));
        
        // Обновляем поля
        existingMaterial.setName(material.getName());
        existingMaterial.setType(material.getType());
        existingMaterial.setColor(material.getColor());
        existingMaterial.setWidthMm(material.getWidthMm());
        existingMaterial.setMinStockLevelMeters(material.getMinStockLevelMeters());
        existingMaterial.setPurchasePricePerMeter(material.getPurchasePricePerMeter());
        existingMaterial.setSellingPricePerMeter(material.getSellingPricePerMeter());
        existingMaterial.setSupplier(material.getSupplier());
        existingMaterial.setMaterialComposition(material.getMaterialComposition());
        existingMaterial.setTexture(material.getTexture());
        existingMaterial.setIsWaterproof(material.getIsWaterproof());
        existingMaterial.setNotes(material.getNotes());
        
        return materialRepository.save(existingMaterial);
    }

    @Override
    public void deactivateMaterial(Long id) {
        log.info("Деактивация материала с ID: {}", id);
        
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Материал не найден с ID: " + id));
        
        material.setIsActive(false);
        materialRepository.save(material);
        
        log.info("Материал деактивирован: {}", material.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getMaterialsByType(MaterialType type) {
        log.debug("Поиск материалов по типу: {}", type);
        return materialRepository.findByTypeAndIsActiveTrue(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getMaterialsByColor(String color) {
        log.debug("Поиск материалов по цвету: {}", color);
        return materialRepository.findByColorContainingIgnoreCaseAndIsActiveTrue(color);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getMaterialsNeedingRestock() {
        log.debug("Поиск материалов, требующих пополнения");
        return materialRepository.findMaterialsNeedingRestock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> searchMaterials(String searchTerm) {
        log.debug("Поиск материалов по запросу: {}", searchTerm);
        return materialRepository.searchByName(searchTerm);
    }

    @Override
    public boolean reserveMaterials(Long materialId, BigDecimal quantity) {
        log.info("Резервирование {} метров материала с ID: {}", quantity, materialId);
        
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Материал не найден с ID: " + materialId));
        
        if (material.getAvailableStockMeters().compareTo(quantity) < 0) {
            log.warn("Недостаточно материала для резервирования. Доступно: {}, запрошено: {}", 
                    material.getAvailableStockMeters(), quantity);
            return false;
        }
        
        material.setReservedStockMeters(material.getReservedStockMeters().add(quantity));
        materialRepository.save(material);
        
        log.info("Зарезервировано {} метров материала: {}", quantity, material.getName());
        return true;
    }

    @Override
    public boolean releaseReservation(Long materialId, BigDecimal quantity) {
        log.info("Освобождение резерва {} метров материала с ID: {}", quantity, materialId);
        
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Материал не найден с ID: " + materialId));
        
        if (material.getReservedStockMeters().compareTo(quantity) < 0) {
            log.warn("Попытка освободить больше резерва чем есть. Зарезервировано: {}, запрошено к освобождению: {}", 
                    material.getReservedStockMeters(), quantity);
            return false;
        }
        
        material.setReservedStockMeters(material.getReservedStockMeters().subtract(quantity));
        materialRepository.save(material);
        
        log.info("Освобожден резерв {} метров материала: {}", quantity, material.getName());
        return true;
    }

    @Override
    public Material updateStock(Long materialId, BigDecimal quantityChange, String reason) {
        log.info("Обновление остатка материала с ID: {} на {} метров. Причина: {}", 
                materialId, quantityChange, reason);
        
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Материал не найден с ID: " + materialId));
        
        BigDecimal newStock = material.getCurrentStockMeters().add(quantityChange);
        if (newStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Остаток не может быть отрицательным");
        }
        
        material.setCurrentStockMeters(newStock);
        
        Material savedMaterial = materialRepository.save(material);
        log.info("Остаток материала {} обновлен. Новый остаток: {} метров", 
                material.getName(), newStock);
        
        return savedMaterial;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getMaterialStatisticsByType() {
        log.debug("Получение статистики по типам материалов");
        return materialRepository.getMaterialStatisticsByType();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalStockValue() {
        log.debug("Получение общей стоимости остатков материалов");
        return materialRepository.getTotalStockValue();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(Long materialId, BigDecimal requiredQuantity) {
        log.debug("Проверка доступности {} метров материала с ID: {}", requiredQuantity, materialId);
        
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Материал не найден с ID: " + materialId));
        
        return material.getAvailableStockMeters().compareTo(requiredQuantity) >= 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getMaterialsBySupplier(String supplier) {
        log.debug("Поиск материалов по поставщику: {}", supplier);
        return materialRepository.findBySupplierContainingIgnoreCaseAndIsActiveTrue(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getWaterproofMaterials() {
        log.debug("Поиск водостойких материалов");
        return materialRepository.findByIsWaterproofAndIsActiveTrue(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getMaterialsByWidthRange(Integer minWidth, Integer maxWidth) {
        log.debug("Поиск материалов по диапазону ширины: {} - {} мм", minWidth, maxWidth);
        return materialRepository.findByWidthRange(minWidth, maxWidth);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getMaterialsByComposition(String composition) {
        log.debug("Поиск материалов по составу: {}", composition);
        return materialRepository.findByMaterialCompositionContainingIgnoreCaseAndIsActiveTrue(composition);
    }
}
