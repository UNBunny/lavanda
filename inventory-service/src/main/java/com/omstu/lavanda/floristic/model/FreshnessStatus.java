package com.omstu.lavanda.floristic.model;

/**
 * Статус свежести цветов для флористического учета LAVANDA ERP
 */
public enum FreshnessStatus {
    FRESH("Свежий"),
    WARNING("Требует внимания"),
    CRITICAL("Критический"),
    EXPIRES_TODAY("Истекает сегодня"),
    EXPIRED("Просрочен"),
    UNKNOWN("Неизвестно");

    private final String displayName;

    FreshnessStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
