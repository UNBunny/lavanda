package com.omstu.lavanda.floristic.service;

import com.omstu.lavanda.floristic.model.Material;
import com.omstu.lavanda.floristic.model.MaterialType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с материалами в флористическом учете LAVANDA ERP
 */
public interface MaterialService {

    /**
     * Получить все активные материалы
     */
    List<Material> getAllActiveMaterials();

    /**
     * Найти материал по ID
     */
    Optional<Material> getMaterialById(Long id);

    /**
     * Создать новый материал
     */
    Material createMaterial(Material material);

    /**
     * Обновить материал
     */
    Material updateMaterial(Long id, Material material);

    /**
     * Деактивировать материал
     */
    void deactivateMaterial(Long id);

    /**
     * Найти материалы по типу
     */
    List<Material> getMaterialsByType(MaterialType type);

    /**
     * Найти материалы по цвету
     */
    List<Material> getMaterialsByColor(String color);

    /**
     * Найти материалы с низким остатком
     */
    List<Material> getMaterialsNeedingRestock();

    /**
     * Поиск материалов по названию
     */
    List<Material> searchMaterials(String searchTerm);

    /**
     * Зарезервировать материалы под заказ
     */
    boolean reserveMaterials(Long materialId, BigDecimal quantity);

    /**
     * Освободить резерв материалов
     */
    boolean releaseReservation(Long materialId, BigDecimal quantity);

    /**
     * Обновить остаток материалов (поступление/списание)
     */
    Material updateStock(Long materialId, BigDecimal quantityChange, String reason);

    /**
     * Получить статистику по типам материалов
     */
    List<Object[]> getMaterialStatisticsByType();

    /**
     * Получить общую стоимость остатков материалов
     */
    Double getTotalStockValue();

    /**
     * Проверить доступность материалов для заказа
     */
    boolean checkAvailability(Long materialId, BigDecimal requiredQuantity);

    /**
     * Получить материалы по поставщику
     */
    List<Material> getMaterialsBySupplier(String supplier);

    /**
     * Получить водостойкие материалы
     */
    List<Material> getWaterproofMaterials();

    /**
     * Найти материалы по диапазону ширины
     */
    List<Material> getMaterialsByWidthRange(Integer minWidth, Integer maxWidth);

    /**
     * Найти материалы по составу
     */
    List<Material> getMaterialsByComposition(String composition);
}
