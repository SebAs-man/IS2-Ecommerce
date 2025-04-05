package com.ecommerce.catalog.sharedkernel.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Optional;

/**
 * Interfaz genérica para la lectura de entidades en la capa de aplicación.
 * Proporciona métodos para consultar múltiples instancias o una instancia específica
 * en el almacenamiento correspondiente.
 *
 * @param <R> el tipo de la respuesta (DTO) que contiene los datos solicitados.
 * @param <ID> el tipo del identificador único de la entidad a consultar.
 */
public interface ReadService<R, ID extends Serializable> {
    /**
     * Obtiene todas las instancias del objeto en base de datos.
     * @return una lista de objetos DTO de tipo response de las instancias encontradas.
     */
    Page<R> findAll(Pageable pageable);

    /**
     * Obtiene una instancia en específico.
     * @param id es el identificador único de la instancia a buscar.
     * @return un DTO de tipo response de la instancia encontrada.
     */
    Optional<R> findById(ID id);
}
