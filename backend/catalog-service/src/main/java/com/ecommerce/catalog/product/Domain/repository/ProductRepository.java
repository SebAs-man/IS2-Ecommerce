package com.ecommerce.catalog.product.Domain.repository;

import com.ecommerce.catalog.product.Domain.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio del producto para solicitar los datos necesarios en la base de datos.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
}
