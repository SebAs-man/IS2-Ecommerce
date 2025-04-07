package com.ecommerce.catalog.product.application.dto.response;

import com.ecommerce.catalog.sharedkernel.application.dto.MoneyDTO;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Objeto de transferencia de datos (DTO) que representa la estructura de la respuesta de una variante de producto.
 * Encapsula información esencial relativa a una variante específica de un producto.
 * @param id El identificador único de la variante
 * @param productId Identificador único del producto al que pertenece esta variante.
 * @param price Precio de la variante del producto.
 * @param stock Cantidad de la variante del producto disponible en stock.
 * @param available Indica si la variante está actualmente disponible para la venta.
 * @param images Lista de URLs de imágenes asociadas a la variante de producto.
 * @param attributes Mapa que contiene par clave-valor de atributos específicos de la variante.
 * @param version Versión del registro de la variante, utilizada para el seguimiento de actualizaciones.
 */
public record VariantResponseDTO(
        String id,
        String productId,
        MoneyDTO price,
        Integer stock,
        Boolean available,
        List<String> images,
        Map<String, Object> attributes, // Solo guarda selecciones/overrides
        Long version, // para la concurrencia
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
