package com.ecommerce.catalog.product.application.dto.request;

import com.ecommerce.catalog.product.application.dto.AttributeDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * DTO para la petición de creación de un producto.
 * Incluye validaciones para los datos de entrada.
 */
public record ProductRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        @Size(message = "Name must be between 3 & 150 characters")
        String name,

        @NotNull(message = "Price cannot be null.")
        @PositiveOrZero(message = "Price must be zero or positive.")
        Double price,

        @NotNull(message = "Quantity cannot be null.")
        @PositiveOrZero(message = "Quantity must be zero or positive.")
        Integer quantity,

        @Size(max = 5000, message = "Description cannot exceed 5000 characters.")
        String description,

        @Valid //valida las anotaciones de los AttributeDTO de la lista, si los tuviera
        List<AttributeDTO> attributes
) {

}
