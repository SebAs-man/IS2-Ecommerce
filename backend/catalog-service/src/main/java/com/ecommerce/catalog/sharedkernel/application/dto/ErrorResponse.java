package com.ecommerce.catalog.sharedkernel.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO estándar para devolver información sobre errores en la API REST.
 * Usado por GlobalExceptionHandler.
 *
 * @param timestamp        Momento en que ocurrió el error.
 * @param status           Código de estado HTTP.
 * @param error            Descripción corta del estado HTTP (ej: "Not Found").
 * @param message          Mensaje específico del error ocurrido.
 * @param path             URI de la petición que causó el error.
 * @param validationErrors Mapa detallado de errores de validación por campo (solo para errores 400 de validación).
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluir campos nulos en el JSON (ej: validationErrors)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error, // Ej: "Not Found", "Bad Request"
        String message,
        String path,
        // Opcional: para errores de validación
        Map<String, List<String>> validationErrors
) {

    /**
     * Constructor para crear una instancia {@code ErrorResponse} con la marca de tiempo actual.
     *
     * @param status Código de estado HTTP que representa el estado de la respuesta.
     * @param error Breve descripción del estado HTTP (por ejemplo, «No encontrado»).
     * @param message Mensaje de error específico que proporciona contexto adicional.
     * @param path Ruta URI de la petición que causó el error.
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    /**
     * Constructor para crear una instancia {@code ErrorResponse} con la marca de tiempo actual
     * y errores de validación detallados.
     *
     * @param status Código de estado HTTP que representa el estado de la respuesta.
     * @param error Breve descripción del estado HTTP (por ejemplo, «No encontrado»).
     * @param message Mensaje de error específico que proporciona contexto adicional.
     * @param path Ruta URI de la petición que causó el error.
     * @param validationErrors Mapa que contiene errores de validación específicos de cada campo.
     */
    public ErrorResponse(int status, String error, String message, String path, Map<String, List<String>> validationErrors) {
        this(LocalDateTime.now(), status, error, message, path, validationErrors);
    }
}
