package com.ecommerce.catalog.sharedkernel.api;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * Interfaz que proporciona un método para eliminar recursos.
 * @param <ID> el tipo del identificador único del recurso, debe extender Serializable.
 */
public interface DeleteController<ID extends Serializable> {
    /**
     * Elimina un recurso identificado por su identificador único.
     *
     * @param id el identificador único del recurso que se desea eliminar, no debe ser nulo.
     * @return una ResponseEntity con un cuerpo vacío y el estado HTTP correspondiente:
     *         - 200 (OK) si la eliminación fue exitosa.
     *         - 404 (Not Found) si el recurso no se encontró.
     *         - Otros códigos de estado HTTP para errores según corresponda.
     */
    ResponseEntity<Void> delete(ID id);
}
