package com.ecommerce.catalog.product.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa una solicitud de creación de un producto.
 * Este registro encapsula detalles sobre el producto, incluyendo su nombre, descripción,
 * marca, categorías asociadas, atributos estáticos y atributos dinámicos para variantes
 * @param name - El nombre del producto. Debe ser una cadena no en blanco con un máximo de 200 caracteres.
 * @param description - La descripción del producto. Puede tener una longitud máxima de 5000 caracteres o ser nula.
 * @param brandId - Un identificador de la marca asociada al producto. Puede ser nulo.
 * @param categoriesId - Una lista de identificadores de categoría a la que pertenece el producto. Puede estar vacía o ser nula si no se especifican categorías.
 * @param attributeDefinitions - Una lista de atributos que definen posibles opciones de variantes.
 */
public record CreateProductRequestDTO(
        @NotBlank(message = "Name cannot be null.")
        @Size(max = 200, message = "Name must be less than 200 characters.")
        String name,

        @Size(max = 5000, message = "Description must be less than 5000 characters.")
        String description,

        @NotBlank(message = "BrandId cannot be blank.")
        String brandId,

        @NotEmpty(message = "Product must belong to at least one category")
        List<String> categoriesId,

        @Valid
        List<AttributeRequestDTO> attributeDefinitions,

        @NotNull(message = "Product must have at least one variant.")
        @Valid
        CreateVariantRequestDTO initialVariant
) implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
}
