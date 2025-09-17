package com.omstu.lavanda.floristic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO для обновления остатков на складе в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequest {

    @NotNull(message = "Количество обязательно")
    private Integer quantity; // Для цветов (штуки)

    private BigDecimal meters; // Для материалов (метры)

    @NotBlank(message = "Причина изменения обязательна")
    @Size(min = 3, max = 200, message = "Причина должна быть от 3 до 200 символов")
    private String reason;

    @Size(max = 100, message = "Номер партии не должен превышать 100 символов")
    private String batchNumber;

    @Size(max = 500, message = "Примечания не должны превышать 500 символов")
    private String notes;
}
