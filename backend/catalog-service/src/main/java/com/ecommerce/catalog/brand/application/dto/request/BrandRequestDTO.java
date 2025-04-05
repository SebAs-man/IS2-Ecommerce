package com.ecommerce.catalog.brand.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.io.Serial;
import java.io.Serializable;

/**
 * CreateBrandRequestDTO es un objeto de transferencia de datos utilizado para encapsular
 * la información necesaria para crear una nueva marca dentro del sistema.
 * Este registro sirve como modelo de solicitud en operaciones de API o servicios
 * relacionadas con la creación de marcas.
 * Campos:
 * - Nombre: Representa el nombre de la marca. Este campo es obligatorio y tiene una longitud máxima de 100 caracteres.
 * - descripción: Una descripción textual opcional de la marca, que puede tener una longitud máxima de 5000 caracteres.
 * - logoUrl: Una cadena URL opcional que apunta a la ubicación del logotipo de la marca.
 * Esta clase incluye anotaciones de validación para garantizar la integridad de los datos,
 * como la comprobación de valores no en blanco o la limitación de la longitud de los campos de texto.
 */
public record BrandRequestDTO(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    String name,

    @Size(max = 5000, message = "Description must be less than 5000 characters")
    String description,

    @URL(message = "LogoUrl must be valid")
    @Size(max = 2000, message = "LogoUrl must be less than 2000 characters")
    String logoUrl
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
