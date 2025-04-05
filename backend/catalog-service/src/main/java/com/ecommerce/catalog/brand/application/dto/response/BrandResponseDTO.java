package com.ecommerce.catalog.brand.application.dto.response;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representa un objeto de transferencia de datos para devolver información sobre una marca.
 * Esta clase encapsula los detalles de una marca y es típicamente
 * utilizada como objeto de respuesta en llamadas de servicio o API.
 * La clase contiene la siguiente información sobre una marca:
 * - Nombre: El nombre de la marca.
 * - Slug: Un identificador URL amigable para la marca.
 * - Descripción: Una descripción textual de la marca.
 * - logoUrl: URL que enlaza con la imagen del logotipo de la marca.
 * - createdAt: Fecha de creación de la marca.
 * - updatedAt: Fecha y hora de la última actualización de la marca.
 */
public record BrandResponseDTO(
        String id,
        String name,
        String description,
        String logoUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
