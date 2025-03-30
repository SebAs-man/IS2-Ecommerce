package com.ecommerce.catalog.sharedkernel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase manejadora de una excepción.
 * La excepción ocurre cuando se intenta buscar algo que no existe.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Recurso '%s' no encontrado con %s = '%s'", resourceName, fieldName, fieldValue));
    }
}
