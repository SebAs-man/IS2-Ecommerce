package com.ecommerce.catalog.sharedkernel.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaz genérica utilizada para generalizar los métodos de los controladores
 */
public interface GenericController<Q, R, ID extends Serializable> {
    /**
     * Guarda una nueva instancia en la base de datos.
     * @param dto la instancia a guardar con los parámetros necesarios.
     * @return 201 si se guardó correctamente la instancia.
     *          400 si los parámetros enviados son incorrectos o inutilizables.
     */
    ResponseEntity<R> save(@Valid @RequestBody Q dto);

    /**
     * Obtiene una instancia específica guardada en la base de datos.
     * @param id representa el identificador único de la instancia.
     * @return  200 si la solicitud es correcta y encontró la instancia.
     *          400 si la solicitud es incorrecta
     *          404 si la solicitud es correcta, pero no encontró la instancia.
     */
    ResponseEntity<R> getById(@PathVariable ID id);

    /**
     * Obtiene todas las instancias guardadas en la base de datos.
     * Podría tener parámetros de paginación/filtrado
     * @return 200 y la lista de las instancias existentes.
     */
    ResponseEntity<List<R>> getAll();
}
