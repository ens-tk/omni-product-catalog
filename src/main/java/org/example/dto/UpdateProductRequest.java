package org.example.dto;

import org.example.model.ProductType;

import java.time.Instant;

public record UpdateProductRequest(
        ProductType type,
        String name,
        String description,
        Instant openDate,
        Instant closeDate
) {

}
