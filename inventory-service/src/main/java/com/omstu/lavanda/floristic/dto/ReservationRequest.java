package com.omstu.lavanda.floristic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO для резервирования товаров в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @NotNull(message = "Количество обязательно")
    @Min(value = 1, message = "Количество должно быть больше 0")
    private Integer quantity; // Для цветов (штуки)

    @DecimalMin(value = "0.01", message = "Количество метров должно быть больше 0")
    private BigDecimal meters; // Для материалов (метры)

    @NotBlank(message = "Причина резервирования обязательна")
    @Size(min = 3, max = 200, message = "Причина должна быть от 3 до 200 символов")
    private String reservationReason;

    @Size(max = 100, message = "Номер заказа не должен превышать 100 символов")
    private String orderNumber;

    @Size(max = 500, message = "Примечания не должны превышать 500 символов")
    private String notes;
}
