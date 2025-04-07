package com.ecommerce.catalog.product.domain.repository;

import com.ecommerce.catalog.product.domain.model.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder a los datos de la entidad Variant en MongoDB.
 */
@Repository
public interface VariantRepository extends MongoRepository<Variant, String> {
    /**
     * Busca todas las variantes asociadas a un producto padre, paginado.
     * @param productId identificador del producto padre.
     * @param pageable detalles de paginación que especifican el número de página.
     * @return una lista paginada de variantes que pertenecen a un producto.
     */
    Page<Variant> findByProductIdValue(String productId, Pageable pageable);

    /**
     * Elimina todas las variantes de un producto especificado.
     * @param productId el identificador del producto.
     * @return El total de variables eliminadas.
     */
    Long deleteByProductIdValue(String productId);
}
