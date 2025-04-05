package com.ecommerce.catalog.category.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record CreateCategoryRequestDTO (
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,

        @Size(max = 5000, message = "Description must be less than 5000 characters")
        String description,

        String parentId
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
