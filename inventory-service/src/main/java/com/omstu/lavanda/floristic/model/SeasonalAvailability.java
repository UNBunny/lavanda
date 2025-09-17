package com.omstu.lavanda.floristic.model;

/**
 * Сезонная доступность цветов для флористического учета LAVANDA ERP
 */
public enum SeasonalAvailability {
    YEAR_ROUND("Круглогодично"),
    SPRING("Весна"),
    SUMMER("Лето"),
    AUTUMN("Осень"),
    WINTER("Зима"),
    SPRING_SUMMER("Весна-Лето"),
    AUTUMN_WINTER("Осень-Зима"),
    HOLIDAY_SEASON("Праздничный сезон"),
    LIMITED("Ограниченно");

    private final String displayName;

    SeasonalAvailability(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
