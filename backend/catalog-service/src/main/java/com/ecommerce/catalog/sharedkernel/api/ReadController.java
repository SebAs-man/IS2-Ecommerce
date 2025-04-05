package com.ecommerce.catalog.sharedkernel.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * Interfaz que proporciona métodos para leer recursos.
 * @param <R> el tipo de recurso gestionado por el controlador.
 * @param <ID> el tipo del identificador único del recurso, debe extender Serializable
 */
public interface ReadController<R, ID extends Serializable> {
    /**
     * Recupera una lista paginada de todos los recursos.
     * @param pageable la información de paginación y ordenación
     * @return una ResponseEntity que contiene una Page de recursos
     */
    ResponseEntity<Page<R>> getAll(Pageable pageable);

    /**
     * Recupera un recurso por su identificador único.
     * @param id el identificador único del recurso a encontrar
     * @return  una ResponseEntity que contiene el recurso si se encuentra, o una respuesta de error apropiada en caso contrario
     */
    ResponseEntity<R> getById(ID id);
}
