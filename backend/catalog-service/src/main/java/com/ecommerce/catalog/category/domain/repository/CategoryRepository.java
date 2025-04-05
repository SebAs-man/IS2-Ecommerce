package com.ecommerce.catalog.category.domain.repository;

import com.ecommerce.catalog.brand.domain.model.Brand;
import com.ecommerce.catalog.category.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceder a los datos de la entidad Category en MongoDB.
 */
@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    /**
     * Comprueba si existe en la base de datos una entidad Category con el nombre especificado.
     * @param name el nombre de la categoría cuya existencia se comprueba. No debe ser nulo ni estar en blanco.
     * @return true si existe una categoría con el nombre especificado, false en caso contrario.
     */
    Boolean existsByNameValueIgnoreCase(String name);

    /**
     * Busca Categorías cuyos nombres contengan la subcadena especificada, ignorando mayúsculas y minúsculas.
     * @param name la subcadena a buscar dentro de los nombres de categoría. Debe ser no nulo y no estar en blanco.
     * @param pageable información de paginación a aplicar a la consulta. Debe ser no nulo.
     * @return una lista paginada de categorías cuyos nombres contienen la subcadena especificada, ignorando mayúsculas y minúsculas.
     */
    Page<Category> findByNameValueContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Recupera una lista paginada de categorías que no tienen una categoría padre,
     * que representan las categorías de nivel superior o raíz.
     * @param pageable detalles de paginación que especifican el número de página, tamaño y ordenación. No debe ser null.
     * @return una lista paginada de categorías cuyo ID principal es nulo.
     */
    Page<Category> findByParentIdIsNull(Pageable pageable);

    /**
     * Recupera una lista paginada de categorías directas que tienen el ID de categoría padre especificado.
     * @param parentId el identificador único de la categoría padre. No debe ser nulo ni estar en blanco.
     * @param pageable detalles de paginación que especifican el número de página, tamaño y ordenación. No debe ser nulo.
     * @return una lista paginada de categorías que están asociadas con el ID de la categoría padre proporcionado.
     */
    Page<Category> findByParentId(String parentId, Pageable pageable);

    /**
     * Cuenta el número de categorías que tienen el ID de padre especificado.
     * @param parentId el identificador único de la categoría padre. No debe ser nulo ni estar en blanco.
     * @return el número total de categorías asociadas con el ID padre especificado.
     */
    Long countByParentId(String parentId);

    /**
     * Recupera una lista paginada de categorías que son descendientes de la categoría antepasada especificada.
     * @param ancestorId el identificador único de la categoría antecesora. No debe ser nulo ni estar vacío.
     * @param pageable los detalles de paginación que especifican el número de página, el tamaño y la ordenación. No debe ser nulo.
     * @return una lista paginada de categorías que tienen el ancestro especificado en su jerarquía.
     */
    Page<Category> findByAncestors(String ancestorId, Pageable pageable);

    /**
     * Encuentra las categorías antecesoras para un ID de categoría dado.
     * @param categoryId el identificador único de la categoría cuyos ancestros deben ser recuperados.
     * No debe ser nulo ni estar vacío.
     * @return un Opcional que contiene la categoría antecesora si se encuentra o un Opcional vacío en caso contrario.
     */
    Optional<Category> findAncestorsById(String categoryId);

}
