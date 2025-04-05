package com.ecommerce.catalog.product.application.dto.response;

import java.util.List;

/**
 * Representa un objeto de transferencia de datos para un atributo en un producto, encapsulando su clave,
 * estado requerido, bandera de selector de imagen, y valores potenciales disponibles.
 *
 * @param key Identificador único o nombre del atributo.
 * @param isRequired Indica si el atributo es obligatorio.
 * @param imgSelector Indica si el atributo está vinculado a una función de selección de imágenes.
 * @param availableValues Lista de posibles valores que puede contener este atributo.
 */
public record AttributeResponseDTO(
    String key,
    Boolean isRequired,
    Boolean imgSelector,
    List<Object> availableValues
) {
}
