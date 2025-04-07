package com.ecommerce.catalog.product.application.dto.request;

import com.ecommerce.catalog.sharedkernel.application.dto.MoneyDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa una solicitud para actualizar una variante de producto.
 * Encapsula detalles clave sobre la variante de producto, incluyendo su precio, nivel de stock, imágenes y atributos.
 * @param price nuevo precio de la variante
 * @param stock nueva cantidad en inventario de la variante.
 * @param images nueva colección de imágenes usada en la variante.
 * @param available si la variante seguirá estando disponible o no.
 */
public record UpdateVariantRequestDTO(
        @NotNull(message = "Price cannot be null.")
        @Valid
        MoneyDTO price,

        @NotNull(message = "Stock cannot be null.")
        @PositiveOrZero(message = "Stock cannot be less to zero.")
        Integer stock,

        List<String> images,

        Boolean available
) {
}
