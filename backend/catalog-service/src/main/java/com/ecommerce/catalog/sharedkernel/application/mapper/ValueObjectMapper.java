package com.ecommerce.catalog.sharedkernel.application.mapper;

import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;
import org.springframework.stereotype.Component;

/**
 * Clase especializada que proporciona funciones genéricas de mapeo para la conversión de
 * Value Objects (Objetos de valor) a sus representaciones internas y viceversa.
 * Diseñada como componente base para extender y agregar lógica de mapeo personalizada en proyectos específicos.
 */
@Component
public final class ValueObjectMapper {
    /**
     * Convierte un NonBlankString a su valor String interno.
     * @param nbString El Value Object NonBlankString (puede ser null).
     * @return El String interno, o null si nbString es null.
     */
    public String nonBlankStringToString(NonBlankString nbString) {
        return nbString == null ? null : nbString.value();
    }
}
