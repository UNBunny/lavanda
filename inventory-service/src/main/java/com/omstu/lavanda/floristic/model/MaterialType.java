package com.omstu.lavanda.floristic.model;

/**
 * Типы материалов для флористического учета LAVANDA ERP
 */
public enum MaterialType {
    RIBBON("Лента"),
    WRAPPING_PAPER("Упаковочная бумага"),
    CELLOPHANE("Целлофан"),
    ORGANZA("Органза"),
    BURLAP("Мешковина"),
    WIRE("Проволока"),
    TAPE("Скотч"),
    TWINE("Шпагат"),
    FABRIC("Ткань"),
    MESH("Сетка"),
    FOAM("Флористическая пена"),
    OASIS("Оазис"),
    DECORATIVE_ELEMENTS("Декоративные элементы");

    private final String displayName;

    MaterialType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
