package com.omstu.lavanda.floristic.model;

/**
 * Цвета цветов для флористического учета LAVANDA ERP
 */
public enum FlowerColor {
    RED("Красный"),
    WHITE("Белый"),
    PINK("Розовый"),
    YELLOW("Желтый"),
    ORANGE("Оранжевый"),
    PURPLE("Фиолетовый"),
    BLUE("Синий"),
    GREEN("Зеленый"),
    CREAM("Кремовый"),
    PEACH("Персиковый"),
    BURGUNDY("Бордовый"),
    LAVENDER("Лавандовый"),
    CORAL("Коралловый"),
    SALMON("Лососевый"),
    MULTICOLOR("Многоцветный");

    private final String displayName;

    FlowerColor(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
