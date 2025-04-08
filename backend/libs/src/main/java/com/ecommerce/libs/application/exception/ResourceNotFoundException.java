package com.ecommerce.libs.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Excepción lanzada cuando no se encuentra un recurso específico solicitado.
 * Mapea a un estado HTTP 404 Not Found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) // Asocia directamente el status 404
public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor con mensaje personalizado.
     * @param message Mensaje descriptivo del error.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor que formatea un mensaje estándar "Recurso no encontrado".
     * @param resourceName Nombre del tipo de recurso (ej.: "Product", "Category").
     * @param fieldName Nombre del campo usado para buscar (ej.: "id", "slug").
     * @param fieldValue Valor del campo que no se encontró.
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Recurso '%s' no encontrado con %s = '%s'", resourceName, fieldName, fieldValue));
    }
}
