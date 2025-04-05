package com.ecommerce.catalog.product.domain.repository;

import com.ecommerce.catalog.product.domain.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder a los datos de la entidad Product en MongoDB.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
}
