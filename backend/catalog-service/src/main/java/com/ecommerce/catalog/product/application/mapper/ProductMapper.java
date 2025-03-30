package com.ecommerce.catalog.product.application.mapper;

import com.ecommerce.catalog.product.application.dto.AttributeDTO;
import com.ecommerce.catalog.product.application.dto.response.ProductResponseDTO;
import com.ecommerce.catalog.product.domain.model.Attribute;
import com.ecommerce.catalog.product.domain.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Interfaz Mapper gestionada por MapStruct para convertir entre entidades de dominio
 * de Producto/Atributo y sus correspondientes DTOs.
 * componentModel = "spring" permite inyectar este mapper en otros beans de Spring.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    /**
     * Mapeo para Attribute -> AttributeDTO
     * Convierte un Value Object de dominio Attribute a su DTO.
     * @param attribute El objeto de dominio
     * @return El DTO correspondiente
     */
    AttributeDTO toAttributeDTO(Attribute attribute);

    /**
     * Mapeo para List<Attribute> -> List<AttributeDTO>
     * Convierte una lista de Attributes de dominio a una lista de AttributeDTOs.
     * MapStruct usará automáticamente toAttributeDTO para cada elemento.
     * @param attributes Lista de objetos de dominio.
     * @return Lista de DTOs.
     */
    List<AttributeDTO> toAttributesDTO(List<Attribute> attributes);

    /**
     * Mapeo para Product -> ProductResponseDTO
     * Convierte la entidad de dominio Product a su DTO de respuesta ProductResponseDTO.
     * Para la lista 'attributes', usará automáticamente 'attributesToAttributeDTOs'.
     * @param product La entidad Product leída de la base de datos.
     * @return El DTO ProductResponseDTO listo para ser enviado en la API.
     */
    ProductResponseDTO toProductResponseDTO(Product product);
}
