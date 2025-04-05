package com.ecommerce.catalog.product.domain.repository;

import com.ecommerce.catalog.product.domain.model.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder a los datos de la entidad Variant en MongoDB.
 */
@Repository
public interface VariantRepository extends MongoRepository<Variant, String> {
}
