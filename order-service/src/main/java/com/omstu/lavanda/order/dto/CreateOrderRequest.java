package com.omstu.lavanda.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для создания нового заказа в LAVANDA ERP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "Имя клиента обязательно")
    @Size(min = 2, max = 100, message = "Имя клиента должно быть от 2 до 100 символов")
    private String customerName;

    @Pattern(regexp = "^\\+?[78][-\\s]?\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$", message = "Некорректный формат телефона")
    private String customerPhone;

    @Email(message = "Некорректный формат email")
    private String customerEmail;

    private String deliveryAddress;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryDate;

    @DecimalMin(value = "0.0", message = "Скидка не может быть отрицательной")
    @Digits(integer = 8, fraction = 2, message = "Некорректный формат скидки")
    private BigDecimal discountAmount;

    @Size(max = 1000, message = "Комментарий не должен превышать 1000 символов")
    private String notes;

    private String paymentMethod;

    private Long assignedFloristId;

    @Valid
    @NotEmpty(message = "Заказ должен содержать хотя бы одну позицию")
    private List<CreateOrderItemRequest> items;
}
