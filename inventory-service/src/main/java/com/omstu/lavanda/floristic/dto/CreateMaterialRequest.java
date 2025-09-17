package com.omstu.lavanda.floristic.dto;

import com.omstu.lavanda.floristic.model.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO для создания нового материала в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaterialRequest {

    @NotBlank(message = "Название материала обязательно")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String name;

    @NotNull(message = "Тип материала обязателен")
    private MaterialType type;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @NotNull(message = "Цена за метр обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @Digits(integer = 8, fraction = 2, message = "Цена должна иметь не более 8 цифр до запятой и 2 после")
    private BigDecimal pricePerMeter;

    @NotNull(message = "Количество метров на складе обязательно")
    @DecimalMin(value = "0.0", message = "Количество не может быть отрицательным")
    @Digits(integer = 8, fraction = 2, message = "Количество должно иметь не более 8 цифр до запятой и 2 после")
    private BigDecimal stockMeters;

    @NotBlank(message = "Поставщик обязателен")
    @Size(min = 2, max = 100, message = "Название поставщика должно быть от 2 до 100 символов")
    private String supplier;

    @Size(max = 100, message = "Контакт поставщика не должен превышать 100 символов")
    private String supplierContact;

    @Size(max = 50, message = "Цвет не должен превышать 50 символов")
    private String color;

    @Size(max = 50, message = "Ширина не должна превышать 50 символов")
    private String width;

    @Size(max = 100, message = "Материал не должен превышать 100 символов")
    private String material;

    @Size(max = 500, message = "Примечания не должны превышать 500 символов")
    private String notes;

    @Builder.Default
    private Boolean isActive = true;
}
