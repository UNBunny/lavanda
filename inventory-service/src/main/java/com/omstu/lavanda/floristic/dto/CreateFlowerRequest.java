package com.omstu.lavanda.floristic.dto;

import com.omstu.lavanda.floristic.model.FlowerColor;
import com.omstu.lavanda.floristic.model.FlowerType;
import com.omstu.lavanda.floristic.model.SeasonalAvailability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для создания нового цветка в флористическом учете LAVANDA ERP
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlowerRequest {

    @NotBlank(message = "Название цветка обязательно")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String name;

    @NotNull(message = "Тип цветка обязателен")
    private FlowerType type;

    @NotNull(message = "Цвет цветка обязателен")
    private FlowerColor color;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @NotNull(message = "Цена за штуку обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @Digits(integer = 8, fraction = 2, message = "Цена должна иметь не более 8 цифр до запятой и 2 после")
    private BigDecimal pricePerPiece;

    @NotNull(message = "Количество на складе обязательно")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private Integer stockQuantity;

    @NotBlank(message = "Поставщик обязателен")
    @Size(min = 2, max = 100, message = "Название поставщика должно быть от 2 до 100 символов")
    private String supplier;

    @Size(max = 100, message = "Контакт поставщика не должен превышать 100 символов")
    private String supplierContact;

    @Future(message = "Дата истечения должна быть в будущем")
    private LocalDate expiryDate;

    @NotNull(message = "Сезонная доступность обязательна")
    private SeasonalAvailability seasonalAvailability;

    @Size(max = 200, message = "Условия хранения не должны превышать 200 символов")
    private String storageConditions;

    @Size(max = 500, message = "Примечания не должны превышать 500 символов")
    private String notes;

    @Builder.Default
    private Boolean isActive = true;
}
