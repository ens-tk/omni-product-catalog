package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.model.ProductType;

import java.time.Instant;


public record CreateProductRequest (
        @NotNull(message = "Тип продукта обязателен")
        ProductType type,

        @NotBlank(message = "Название продукта обязательно")
        @Size(max = 100, message = "Название не должно превышать 100 символов")
        String name,

        @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
        String description,

        @NotNull(message = "Дата открытия обязательна")
        Instant openDate,

        Instant closeDate
) {
}
