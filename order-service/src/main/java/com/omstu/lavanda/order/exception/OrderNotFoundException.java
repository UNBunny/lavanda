package com.omstu.lavanda.order.exception;

/**
 * Исключение, выбрасываемое когда заказ не найден
 */
public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(String message) {
        super(message);
    }
    
    public OrderNotFoundException(Long orderId) {
        super("Заказ с ID " + orderId + " не найден");
    }
    
    public OrderNotFoundException(String orderNumber, boolean byNumber) {
        super("Заказ с номером " + orderNumber + " не найден");
    }
}
