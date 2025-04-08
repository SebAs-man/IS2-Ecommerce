package com.ecommerce.catalog.product.application.dto.request;

import com.ecommerce.libs.application.dto.MoneyDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Objeto de transferencia de datos (DTO) que representa una solicitud para crear una variante de producto.
 * Encapsula detalles clave sobre la variante de producto, incluyendo su precio, nivel de stock, imágenes y atributos.
 * @param price Precio de la variante.
 * Debe ser un valor no nulo, y debe ser un número positivo o cero.
 * @param stock Cantidad en stock de la variante.
 * Debe ser un número positivo o cero, pero puede ser nulo si no se especifica.
 * @param images Una lista de URL de imágenes que representan la variante.
 * Este campo puede estar vacío o ser nulo si no hay imágenes asociadas.
 * @param attributes Mapa no vacío que define los atributos de la variante,
 * donde la clave representa el nombre del atributo y el valor representa los datos del atributo.
 * Los atributos añaden contexto y características personalizadas a la variante.
 */
public record CreateVariantRequestDTO(
        @NotNull(message = "Price cannot be null.")
        @Valid
        MoneyDTO price,

        @NotNull(message = "Stock cannot be null.")
        @PositiveOrZero(message = "Stock cannot be less to zero.")
        Integer stock,

        List<String> images,

        Map<String, Object> attributes
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
