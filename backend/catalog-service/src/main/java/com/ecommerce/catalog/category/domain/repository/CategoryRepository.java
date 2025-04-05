package com.ecommerce.catalog.category.domain.repository;

import com.ecommerce.catalog.category.domain.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder a los datos de la entidad Category en MongoDB.
 */
@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}
