package com.omstu.lavanda.floristic.service;

import com.omstu.lavanda.floristic.model.Flower;
import com.omstu.lavanda.floristic.model.FlowerColor;
import com.omstu.lavanda.floristic.model.FlowerType;
import com.omstu.lavanda.floristic.model.SeasonalAvailability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с цветами в флористическом учете LAVANDA ERP
 */
public interface FlowerService {

    /**
     * Получить все активные цветы
     */
    List<Flower> getAllActiveFlowers();

    /**
     * Найти цветок по ID
     */
    Optional<Flower> getFlowerById(Long id);

    /**
     * Создать новый цветок
     */
    Flower createFlower(Flower flower);

    /**
     * Обновить цветок
     */
    Flower updateFlower(Long id, Flower flower);

    /**
     * Деактивировать цветок
     */
    void deactivateFlower(Long id);

    /**
     * Найти цветы по типу
     */
    List<Flower> getFlowersByType(FlowerType type);

    /**
     * Найти цветы по цвету
     */
    List<Flower> getFlowersByColor(FlowerColor color);

    /**
     * Найти цветы по типу и цвету
     */
    List<Flower> getFlowersByTypeAndColor(FlowerType type, FlowerColor color);

    /**
     * Найти цветы с низким остатком
     */
    List<Flower> getFlowersNeedingRestock();

    /**
     * Найти цветы с истекающим сроком свежести
     */
    List<Flower> getFlowersExpiringBefore(LocalDate date);

    /**
     * Найти свежие цветы
     */
    List<Flower> getFreshFlowers();

    /**
     * Найти цветы по сезонной доступности
     */
    List<Flower> getFlowersBySeasonalAvailability(SeasonalAvailability seasonalAvailability);

    /**
     * Поиск цветов по названию или сорту
     */
    List<Flower> searchFlowers(String searchTerm);

    /**
     * Зарезервировать цветы под заказ
     */
    boolean reserveFlowers(Long flowerId, Integer quantity);

    /**
     * Освободить резерв цветов
     */
    boolean releaseReservation(Long flowerId, Integer quantity);

    /**
     * Обновить остаток цветов (поступление/списание)
     */
    Flower updateStock(Long flowerId, Integer quantityChange, String reason);

    /**
     * Получить статистику по типам цветов
     */
    List<Object[]> getFlowerStatisticsByType();

    /**
     * Получить общую стоимость остатков цветов
     */
    Double getTotalStockValue();

    /**
     * Проверить доступность цветов для заказа
     */
    boolean checkAvailability(Long flowerId, Integer requiredQuantity);

    /**
     * Получить цветы, истекающие сегодня
     */
    List<Flower> getFlowersExpiringToday();

    /**
     * Получить цветы по поставщику
     */
    List<Flower> getFlowersBySupplier(String supplier);

    /**
     * Получить цветы по стране происхождения
     */
    List<Flower> getFlowersByCountryOrigin(String countryOrigin);
}
