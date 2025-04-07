package com.ecommerce.catalog.product.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa la estructura de respuesta para una entidad de producto.
 * Encapsula detalles esenciales del producto, incluyendo metadatos, categorización y atributos.
 * @param id Identificador único del producto.
 * @param name Nombre del producto.
 * @param description Descripción detallada del producto.
 * @param brandId Identificador de la marca asociada al producto.
 * @param categoriesId Lista de identificadores de las categorías a las que pertenece el producto.
 * @param attributeDefinitions Lista de identificadores o claves de atributos dinámicos asociados al producto.
 */
public record ProductResponseDTO(
        String id,
        String name,
        String description,
        String brandId,
        List<String> categoriesId,
        List<AttributeResponseDTO> attributeDefinitions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
