package com.ecommerce.catalog.product.application.dto.response;

import com.ecommerce.catalog.brand.application.dto.response.BrandResponseDTO;
import com.ecommerce.catalog.category.application.dto.CategoryResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Objeto de transferencia de datos (DTO) que representa la estructura de respuesta para una entidad de producto.
 * Encapsula detalles esenciales del producto, incluyendo metadatos, categorización y atributos.
 *
 * @param name Nombre del producto.
 * @param description Descripción detallada del producto.
 * @param brandId Identificador de la marca asociada al producto.
 * @param categoriesId Lista de identificadores de las categorías a las que pertenece el producto.
 * @param staticAttributes Mapa que contiene par clave-valor de atributos estáticos del producto.
 * @param dynamicAttributes Lista de identificadores o claves de atributos dinámicos asociados al producto.
 */
public record ProductResponseDTO(
    String name,
    String description,
    BrandResponseDTO brandId,
    List<CategoryResponseDTO> categoriesId,
    Map<String, String> staticAttributes,
    List<AttributeResponseDTO> dynamicAttributes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
