package com.ecommerce.catalog.sharedkernel.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error, // Ej: "Not Found", "Bad Request"
        String message,
        String path,
        // Opcional: para errores de validación
        Map<String, List<String>> validationErrors
) {
    // Constructores compactos para simplificar la creación en el handler
    public ErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, List<String>> validationErrors) {
        this(LocalDateTime.now(), status, error, message, path, validationErrors);
    }
}
