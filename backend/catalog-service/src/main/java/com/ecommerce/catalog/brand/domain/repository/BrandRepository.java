package com.ecommerce.catalog.brand.domain.repository;

import com.ecommerce.catalog.brand.domain.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Repositorio para acceder a los datos de la entidad Brand en MongoDB.
 */
@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
    /**
     * Comprueba si existe en la base de datos una entidad Brand con el nombre especificado.
     * @param name el nombre de la Marca cuya existencia se comprueba. No debe ser nulo ni estar en blanco.
     * @return true si existe una Marca con el nombre especificado, false en caso contrario.
     */
    Boolean existsByNameValueIgnoreCase(String name);

    /**
     * Busca marcas cuyos nombres contengan la subcadena especificada, ignorando mayúsculas y minúsculas.
     * @param name la subcadena a buscar dentro de los nombres de marca. Debe ser no nulo y no estar en blanco.
     * @param pageable información de paginación a aplicar a la consulta. Debe ser no nulo.
     * @return una lista paginada de marcas cuyos nombres contienen la subcadena especificada, ignorando mayúsculas y minúsculas.
     */
    @Query("{ 'name.value' : { $regex: ?0, $options: 'i' } }")
    Page<Brand> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
