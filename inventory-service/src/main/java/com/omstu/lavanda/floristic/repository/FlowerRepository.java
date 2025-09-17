package com.omstu.lavanda.floristic.repository;

import com.omstu.lavanda.floristic.model.Flower;
import com.omstu.lavanda.floristic.model.FlowerColor;
import com.omstu.lavanda.floristic.model.FlowerType;
import com.omstu.lavanda.floristic.model.SeasonalAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с цветами в флористическом учете LAVANDA ERP
 */
@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {

    /**
     * Найти все активные цветы
     */
    List<Flower> findByIsActiveTrue();

    /**
     * Найти цветы по типу
     */
    List<Flower> findByTypeAndIsActiveTrue(FlowerType type);

    /**
     * Найти цветы по цвету
     */
    List<Flower> findByColorAndIsActiveTrue(FlowerColor color);

    /**
     * Найти цветы по типу и цвету
     */
    List<Flower> findByTypeAndColorAndIsActiveTrue(FlowerType type, FlowerColor color);

    /**
     * Найти цветы с низким остатком (требующие пополнения)
     */
    @Query("SELECT f FROM Flower f WHERE f.isActive = true AND " +
           "f.minStockLevel IS NOT NULL AND " +
           "(f.currentStock - f.reservedStock) <= f.minStockLevel")
    List<Flower> findFlowersNeedingRestock();

    /**
     * Найти цветы с истекающим сроком свежести
     */
    @Query("SELECT f FROM Flower f WHERE f.isActive = true AND " +
           "f.expiryDate IS NOT NULL AND f.expiryDate <= :date")
    List<Flower> findFlowersExpiringBefore(@Param("date") LocalDate date);

    /**
     * Найти свежие цветы (не истекшие)
     */
    @Query("SELECT f FROM Flower f WHERE f.isActive = true AND " +
           "(f.expiryDate IS NULL OR f.expiryDate > CURRENT_DATE)")
    List<Flower> findFreshFlowers();

    /**
     * Найти цветы по сезонной доступности
     */
    List<Flower> findBySeasonalAvailabilityAndIsActiveTrue(SeasonalAvailability seasonalAvailability);

    /**
     * Найти цветы по поставщику
     */
    List<Flower> findBySupplierContainingIgnoreCaseAndIsActiveTrue(String supplier);

    /**
     * Найти цветы по стране происхождения
     */
    List<Flower> findByCountryOriginContainingIgnoreCaseAndIsActiveTrue(String countryOrigin);

    /**
     * Найти цветы с доступным остатком больше указанного количества
     */
    @Query("SELECT f FROM Flower f WHERE f.isActive = true AND " +
           "(f.currentStock - f.reservedStock) >= :minQuantity")
    List<Flower> findFlowersWithAvailableStock(@Param("minQuantity") Integer minQuantity);

    /**
     * Поиск цветов по названию или сорту
     */
    @Query("SELECT f FROM Flower f WHERE f.isActive = true AND " +
           "(LOWER(f.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.variety) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Flower> searchByNameOrVariety(@Param("searchTerm") String searchTerm);

    /**
     * Получить статистику по типам цветов
     */
    @Query("SELECT f.type, COUNT(f), SUM(f.currentStock), SUM(f.reservedStock) " +
           "FROM Flower f WHERE f.isActive = true GROUP BY f.type")
    List<Object[]> getFlowerStatisticsByType();

    /**
     * Получить общую стоимость остатков цветов
     */
    @Query("SELECT SUM(f.currentStock * f.purchasePrice) FROM Flower f " +
           "WHERE f.isActive = true AND f.purchasePrice IS NOT NULL")
    Double getTotalStockValue();

    /**
     * Найти цветы, поступившие в указанный период
     */
    List<Flower> findByDeliveryDateBetweenAndIsActiveTrue(LocalDate startDate, LocalDate endDate);

    /**
     * Подсчитать количество цветов по статусу свежести
     */
    @Query("SELECT COUNT(f) FROM Flower f WHERE f.isActive = true AND " +
           "(f.expiryDate IS NULL OR f.expiryDate > CURRENT_DATE)")
    Long countFreshFlowers();

    @Query("SELECT COUNT(f) FROM Flower f WHERE f.isActive = true AND " +
           "f.expiryDate IS NOT NULL AND f.expiryDate <= CURRENT_DATE")
    Long countExpiredFlowers();
}
