package com.omstu.lavanda.floristic.model;

/**
 * Типы цветов для флористического учета LAVANDA ERP
 */
public enum FlowerType {
    ROSE("Роза"),
    TULIP("Тюльпан"),
    LILY("Лилия"),
    CHRYSANTHEMUM("Хризантема"),
    PEONY("Пион"),
    IRIS("Ирис"),
    CARNATION("Гвоздика"),
    GERBERA("Гербера"),
    ALSTROEMERIA("Альстромерия"),
    FREESIA("Фрезия"),
    SUNFLOWER("Подсолнух"),
    HYDRANGEA("Гортензия"),
    ORCHID("Орхидея"),
    ANTHURIUM("Антуриум"),
    PROTEA("Протея");

    private final String displayName;

    FlowerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
