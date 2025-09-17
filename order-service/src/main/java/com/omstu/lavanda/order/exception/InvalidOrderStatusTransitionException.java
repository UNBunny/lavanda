package com.omstu.lavanda.order.exception;

import com.omstu.lavanda.order.model.OrderStatus;

/**
 * Исключение, выбрасываемое при недопустимом переходе статуса заказа
 */
public class InvalidOrderStatusTransitionException extends RuntimeException {
    
    public InvalidOrderStatusTransitionException(String message) {
        super(message);
    }
    
    public InvalidOrderStatusTransitionException(OrderStatus currentStatus, OrderStatus newStatus) {
        super(String.format("Недопустимый переход статуса с %s на %s", 
                currentStatus.getDisplayName(), newStatus.getDisplayName()));
    }
}
