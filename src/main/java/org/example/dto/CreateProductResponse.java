package org.example.dto;

import org.example.model.ProductType;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.UUID;

public record CreateProductResponse (
        UUID id,
        ProductType type,
        String name,
        String description,
        LocalDate openDate,
        LocalDate closeDate
){
}
