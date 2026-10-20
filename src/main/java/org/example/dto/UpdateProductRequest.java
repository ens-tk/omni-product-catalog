package org.example.dto;

import org.example.model.ProductType;

import java.time.LocalDate;

public record UpdateProductRequest(
        ProductType type,
        String name,
        String description,
        LocalDate openDate,
        LocalDate closeDate
) {

}
