package com.ecommerce.catalog.product.application.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Objeto de transferencia de datos (DTO) que representa una solicitud para crear o actualizar una variante de producto.
 * Encapsula detalles clave sobre la variante de producto, incluyendo su SKU, precio, nivel de stock, imágenes y atributos.
 *
 * @param sku Unidad de mantenimiento de existencias (SKU) de la variante.
 * Debe ser una cadena no en blanco con una longitud entre 3 y 50 caracteres.
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
public record VariantRequestDTO(
        @NotBlank @Size(min = 3, max = 50) String sku,
        @NotNull @PositiveOrZero BigDecimal price,
        @PositiveOrZero Integer stock,
        List<String> images,
        @NotEmpty Map<String, Object> attributes
) {
}
