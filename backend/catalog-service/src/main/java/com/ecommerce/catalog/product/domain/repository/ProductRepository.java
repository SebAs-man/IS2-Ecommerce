package com.ecommerce.catalog.product.domain.repository;

import com.ecommerce.catalog.category.domain.model.Category;
import com.ecommerce.catalog.product.domain.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Repositorio para acceder a los datos de la entidad Product en MongoDB.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    /**
     * Busca Productos cuyos nombres contengan la subcadena especificada, ignorando mayúsculas y minúsculas.
     * @param name la subcadena a buscar dentro de los nombres de producto. Debe ser no nulo y no estar en blanco.
     * @param pageable información de paginación a aplicar a la consulta. Debe ser no nulo.
     * @return una lista paginada de productos cuyos nombres contienen la subcadena especificada, ignorando mayúsculas y minúsculas.
     */
    Page<Product> findByNameValueContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Busca Productos que contengan un identificador de la marca especificada.
     * @param brandId identificador de la marca.
     * @param pageable información de paginación a aplicar la consulta.
     * @return una lista paginada de productos cuyos marcas contienen la subcadena especificada.
     */
    Page<Product> findByBrandIdValue(String brandId, Pageable pageable);

    /**
     * Busca productos que estén asignados a CUALQUIERA de las categorías
     * cuyos ID se proporcionan en la colección, devolviendo resultados paginados.
     * Utiliza el operador $in de MongoDB sobre el campo indexado categoryIds.
     * @param categoriesId Colección de IDs de categorías a buscar.
     * @param pageable Información de paginación y ordenación para los productos.
     * @return Una página de entidades Product que coinciden.
     */
    @Query("{ 'categoriesId' : { $in: ?0 } }")
    Page<Product> findByCategoriesIdIn(Collection<String> categoriesId, Pageable pageable);
}
