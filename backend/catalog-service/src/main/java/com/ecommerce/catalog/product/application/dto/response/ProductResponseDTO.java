package com.ecommerce.catalog.product.application.dto.response;

import com.ecommerce.catalog.product.application.dto.AttributeDTO;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * DTO utilizado para las respuestas de la API
 */
public record ProductResponseDTO(
        ObjectId id, //El ObjectID interno
        String name,
        Double price,
        Integer quantity,
        String description,
        Date created,
        Date updated,
        List<AttributeDTO> attributes //atributos dinámicos
) {
}
