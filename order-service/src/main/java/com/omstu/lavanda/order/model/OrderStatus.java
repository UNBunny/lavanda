package com.omstu.lavanda.order.model;

/**
 * Статусы заказа в цветочной мастерской LAVANDA
 */
public enum OrderStatus {
    /**
     * Новый заказ, только что создан
     */
    NEW("Новый"),
    
    /**
     * Заказ подтвержден и принят в работу
     */
    CONFIRMED("Подтвержден"),
    
    /**
     * Заказ в процессе выполнения (флорист работает над букетом)
     */
    IN_PROGRESS("В работе"),
    
    /**
     * Заказ готов к выдаче/доставке
     */
    READY("Готов"),
    
    /**
     * Заказ передан курьеру для доставки
     */
    OUT_FOR_DELIVERY("В доставке"),
    
    /**
     * Заказ успешно доставлен клиенту
     */
    DELIVERED("Доставлен"),
    
    /**
     * Заказ отменен
     */
    CANCELLED("Отменен"),
    
    /**
     * Заказ возвращен (не забран клиентом)
     */
    RETURNED("Возвращен");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
