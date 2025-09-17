package com.omstu.lavanda.floristic.dto;

import com.omstu.lavanda.floristic.model.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO для обновления материала в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaterialRequest {

    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String name;

    private MaterialType type;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @Digits(integer = 8, fraction = 2, message = "Цена должна иметь не более 8 цифр до запятой и 2 после")
    private BigDecimal pricePerMeter;

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

    private Boolean isActive;
}
