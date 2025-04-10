package com.ecommerce.catalog.category.application.dto.response;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa un objeto de transferencia de datos (DTO) para devolver información sobre categorías.
 * Este registro se utiliza para encapsular detalles sobre una categoría, incluyendo su
 * nombre, descripción, estructura jerárquica a través de ancestros, y marcas de tiempo
 * de creación y última actualización.
 * Campos:
 * - nombre: El nombre de la categoría.
 * - descripción: Una breve descripción de la categoría.
 * - ancestors: Una lista que representa la jerarquía de categorías antecesoras para esta categoría.
 * - createdAt: La fecha y hora de creación de la categoría.
 * - updatedAt: Fecha y hora de la última actualización de la categoría.
 */
public record CategoryResponseDTO (
        String id,
        String name,
        String description,
        String parentId,
        List<String> ancestors, // Mostrar la jerarquía completa
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
