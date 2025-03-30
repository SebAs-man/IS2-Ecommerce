package com.ecommerce.catalog.sharedkernel.application;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Servicio genérico que define los posibles endpoints de un CRUD.
 * @param <Q> define el tipo de dato para los request que maneje el controlador.
 * @param <R> define el tipo de dato para los response que retorne el controlador.
 * @param <ID> define el tipo de dato del identificador único del elemento a ser buscado en la base de datos.
 */
public interface GenericService<Q, R, ID extends Serializable> {
    /**
     * Obtiene todas las instancias del objeto en base de datos.
     * @return una lista de objetos DTO de tipo response de las instancias encontradas.
     */
    List<R> findAll();

    /**
     * Obtiene una instancia en específico.
     * @param id es el identificador único de la instancia a buscar.
     * @return un DTO de tipo response de la instancia encontrada.
     */
    Optional<R> findById(ID id);

    /**
     * Crea una nueva instancia en la base de datos.
     * @param request un objeto DTO de tipo request de la instancia a crear.
     * @return un objeto DTO de tipo response de la instancia creado.
     */
    R save(Q request);
}
