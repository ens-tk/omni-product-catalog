package org.example.model;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
public record Product (
        @Id
        UUID id,
        ProductType type,
        String name,
        String description,
        LocalDate openDate,
        LocalDate closeDate
){}