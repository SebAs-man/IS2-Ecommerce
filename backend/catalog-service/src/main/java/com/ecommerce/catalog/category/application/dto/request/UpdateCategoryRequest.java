package com.ecommerce.catalog.category.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

/**
 * Representa una solicitud para actualizar una categoría existente en el catálogo.
 * Este registro se utiliza para transferir datos relacionados con la operación de actualización de la categoría.
 * Los campos incluyen:
 * - nombre: El nombre actualizado de la categoría, obligatorio y no debe superar los 100 caracteres.
 * - descripción: La descripción actualizada de la categoría, opcional y no debe superar los 5000 caracteres.
 * Implementa Serializable para la serialización de objetos.
 */
public record UpdateCategoryRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,

        @Size(max = 5000, message = "Description must be less than 5000 characters")
        String description
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
