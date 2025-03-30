package com.ecommerce.catalog.product.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para los atributos creados o a mostrar.
 * @param key representa la llave o clave del atributo.
 * @param value representa el valor o la data contenida en el atributo.
 */
public record AttributeDTO(
        @NotBlank(message = "Key cannot be blank.")
        String key,

        @NotNull(message = "Value cannot be null.")
        Object value
) {
}
