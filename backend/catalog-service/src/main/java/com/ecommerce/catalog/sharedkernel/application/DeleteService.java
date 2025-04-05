package com.ecommerce.catalog.sharedkernel.application;

import java.io.Serializable;

/**
 * Interfaz genérica para la eliminación de entidades en la capa de aplicación.
 * Proporciona un método para eliminar una instancia identificada por su identificador único.
 * Su implementación debe garantizar que se gestione correctamente la eliminación de la entidad
 * dentro de la base de datos o el almacenamiento correspondiente.
 *
 * @param <ID> el tipo del identificador único de la entidad que se desea eliminar.
 */
public interface DeleteService<ID extends Serializable> {
    /**
     * Elimina una instancia identificada por su identificador único de la base de datos.
     *
     * @param id el identificador único de la instancia a eliminar.
     */
    void delete(ID id);
}
