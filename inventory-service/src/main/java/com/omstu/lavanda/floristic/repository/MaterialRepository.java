package com.omstu.lavanda.floristic.repository;

import com.omstu.lavanda.floristic.model.Material;
import com.omstu.lavanda.floristic.model.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Репозиторий для работы с материалами в флористическом учете LAVANDA ERP
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    /**
     * Найти все активные материалы
     */
    List<Material> findByIsActiveTrue();

    /**
     * Найти материалы по типу
     */
    List<Material> findByTypeAndIsActiveTrue(MaterialType type);

    /**
     * Найти материалы по цвету
     */
    List<Material> findByColorContainingIgnoreCaseAndIsActiveTrue(String color);

    /**
     * Найти материалы с низким остатком (требующие пополнения)
     */
    @Query("SELECT m FROM Material m WHERE m.isActive = true AND " +
           "m.minStockLevelMeters IS NOT NULL AND " +
           "(m.currentStockMeters - m.reservedStockMeters) <= m.minStockLevelMeters")
    List<Material> findMaterialsNeedingRestock();

    /**
     * Найти материалы с доступным остатком больше указанного количества
     */
    @Query("SELECT m FROM Material m WHERE m.isActive = true AND " +
           "(m.currentStockMeters - m.reservedStockMeters) >= :minQuantity")
    List<Material> findMaterialsWithAvailableStock(@Param("minQuantity") BigDecimal minQuantity);

    /**
     * Поиск материалов по названию
     */
    @Query("SELECT m FROM Material m WHERE m.isActive = true AND " +
           "LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Material> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Найти материалы по поставщику
     */
    List<Material> findBySupplierContainingIgnoreCaseAndIsActiveTrue(String supplier);

    /**
     * Найти материалы по составу
     */
    List<Material> findByMaterialCompositionContainingIgnoreCaseAndIsActiveTrue(String composition);

    /**
     * Найти водостойкие материалы
     */
    List<Material> findByIsWaterproofAndIsActiveTrue(Boolean isWaterproof);

    /**
     * Найти материалы по ширине
     */
    @Query("SELECT m FROM Material m WHERE m.isActive = true AND " +
           "m.widthMm >= :minWidth AND m.widthMm <= :maxWidth")
    List<Material> findByWidthRange(@Param("minWidth") Integer minWidth, 
                                   @Param("maxWidth") Integer maxWidth);

    /**
     * Получить статистику по типам материалов
     */
    @Query("SELECT m.type, COUNT(m), SUM(m.currentStockMeters), SUM(m.reservedStockMeters) " +
           "FROM Material m WHERE m.isActive = true GROUP BY m.type")
    List<Object[]> getMaterialStatisticsByType();

    /**
     * Получить общую стоимость остатков материалов
     */
    @Query("SELECT SUM(m.currentStockMeters * m.purchasePricePerMeter) FROM Material m " +
           "WHERE m.isActive = true AND m.purchasePricePerMeter IS NOT NULL")
    Double getTotalStockValue();

    /**
     * Найти материалы по диапазону цен
     */
    @Query("SELECT m FROM Material m WHERE m.isActive = true AND " +
           "m.sellingPricePerMeter >= :minPrice AND m.sellingPricePerMeter <= :maxPrice")
    List<Material> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Подсчитать общее количество метров материалов по типу
     */
    @Query("SELECT SUM(m.currentStockMeters) FROM Material m " +
           "WHERE m.isActive = true AND m.type = :type")
    BigDecimal getTotalMetersByType(@Param("type") MaterialType type);
}
