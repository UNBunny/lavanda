package com.omstu.lavanda.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для создания позиции заказа в LAVANDA ERP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderItemRequest {

    @NotNull(message = "ID товара обязателен")
    private Long productId;

    @NotBlank(message = "Название товара обязательно")
    @Size(min = 1, max = 200, message = "Название товара должно быть от 1 до 200 символов")
    private String productName;

    private String productSku;

    private String productType;

    @NotNull(message = "Количество обязательно")
    @DecimalMin(value = "0.001", message = "Количество должно быть больше 0")
    @Digits(integer = 7, fraction = 3, message = "Некорректный формат количества")
    private BigDecimal quantity;

    @NotBlank(message = "Единица измерения обязательна")
    @JsonProperty("unit")
    private String unitOfMeasure;

    @NotNull(message = "Цена за единицу обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @Digits(integer = 8, fraction = 2, message = "Некорректный формат цены")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.0", message = "Скидка не может быть отрицательной")
    @Digits(integer = 8, fraction = 2, message = "Некорректный формат скидки")
    private BigDecimal discountAmount;

    @Size(max = 500, message = "Комментарий не должен превышать 500 символов")
    private String notes;

    private Boolean isBouquetComponent;

    private Long parentBouquetId;
}
