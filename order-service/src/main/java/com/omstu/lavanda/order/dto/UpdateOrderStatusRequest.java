package com.omstu.lavanda.order.dto;

import com.omstu.lavanda.order.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления статуса заказа в LAVANDA ERP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequest {

    @NotNull(message = "Новый статус обязателен")
    private OrderStatus newStatus;

    @Size(max = 500, message = "Комментарий не должен превышать 500 символов")
    private String comment;

    private Long assignedFloristId;
}
