package com.omstu.lavanda.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.omstu.lavanda.order.model.OrderStatus;
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
 * DTO для заказа в API LAVANDA ERP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    private String orderNumber;

    private OrderStatus status;

    @NotBlank(message = "Имя клиента обязательно")
    @Size(min = 2, max = 100, message = "Имя клиента должно быть от 2 до 100 символов")
    private String customerName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Некорректный формат телефона")
    private String customerPhone;

    @Email(message = "Некорректный формат email")
    private String customerEmail;

    private String deliveryAddress;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryDate;

    @NotNull(message = "Общая стоимость обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Стоимость должна быть больше 0")
    @Digits(integer = 8, fraction = 2, message = "Некорректный формат стоимости")
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Скидка не может быть отрицательной")
    @Digits(integer = 8, fraction = 2, message = "Некорректный формат скидки")
    private BigDecimal discountAmount;

    @NotNull(message = "Итоговая стоимость обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Итоговая стоимость должна быть больше 0")
    @Digits(integer = 8, fraction = 2, message = "Некорректный формат итоговой стоимости")
    private BigDecimal finalAmount;

    @Size(max = 1000, message = "Комментарий не должен превышать 1000 символов")
    private String notes;

    private String paymentMethod;

    private String paymentStatus;

    private Long assignedFloristId;

    @Valid
    @NotEmpty(message = "Заказ должен содержать хотя бы одну позицию")
    private List<OrderItemDto> items;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
