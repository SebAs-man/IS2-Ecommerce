package com.ecommerce.catalog.product.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Objeto de transferencia de datos (DTO) que representa una solicitud de actualización de un producto.
 * Este registro encapsula detalles sobre el producto, incluyendo su nombre, descripción,
 * marca, categorías asociadas, atributos estáticos y atributos dinámicos para variantes
 * @param name El nuevo nombre del producto.
 * @param description La nueva descripción.
 * @param brandId El nuevo identificador de la marca del producto.
 * @param categoriesId Una nueva lista de identificadores de categorías a las que pertenece el producto.
 */
public record UpdateProductRequestDTO(
        @NotBlank(message = "Name cannot be blank.")
        @Size(max = 200, message = "Name must be less than 200 characters.")
        String name,

        @Size(max = 5000, message = "Description must be less than 5000 characters.")
        String description,

        @NotBlank(message = "BrandId cannot be null.")
        String brandId,

        @NotEmpty(message = "product must belong to at least one category")
        List<String> categoriesId
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
