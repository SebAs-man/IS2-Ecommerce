package com.ecommerce.catalog.category.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Representa un objeto de transferencia de datos para crear una categoría.
 * Este registro se utiliza para encapsular los detalles necesarios para crear una categoría en el sistema.
 * Campos:
 * - Nombre: Nombre de la categoría. No debe estar en blanco y tiene una restricción de longitud máxima.
 * - Descripción: Una descripción opcional de la categoría con una restricción de longitud máxima.
 * - parentId: El identificador de la categoría padre, si procede, para establecer una jerarquía de categorías.
 */
public record CreateCategoryRequestDTO (
        @NotBlank @Size(max = 100) String name,
        @Size(max = 5000) String description,
        String parentId // Id (String) de la categoría padre, null de ser el padre.
) {}
