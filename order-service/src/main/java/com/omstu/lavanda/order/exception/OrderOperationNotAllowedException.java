package com.omstu.lavanda.order.exception;

/**
 * Исключение, выбрасываемое когда операция с заказом не разрешена
 */
public class OrderOperationNotAllowedException extends RuntimeException {
    
    public OrderOperationNotAllowedException(String message) {
        super(message);
    }
    
    public OrderOperationNotAllowedException(String operation, Long orderId) {
        super(String.format("Операция '%s' не разрешена для заказа с ID %d", operation, orderId));
    }
}
