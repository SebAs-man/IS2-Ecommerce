package com.ecommerce.catalog.product.application.dto.request;

import com.ecommerce.catalog.product.domain.constant.AttributeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) para representar una solicitud de atributo.
 * Esta clase encapsula los datos necesarios para definir o solicitar un atributo,
 * incluyendo metadatos como tipo, clave y restricciones opcionales.
 *
 * @param key Identificador único del atributo. Debe ser una cadena que no esté en blanco.
 * @param type El tipo de atributo, representado por la enumeración {@code AttributeType}.
 * No puede ser nulo y determina el formato o la validación de datos para el valor del atributo.
 * @param isRequired Bandera que indica si el atributo es obligatorio.
 * Si es true, el atributo debe tener un valor.
 * @param imgSelector Bandera que especifica si el atributo está vinculado a una selección de imagen o funcionalidad relacionada.
 * @param availableValues Lista opcional de valores predefinidos aceptables para el atributo.
 * Esto permite restringir los valores permitidos que puede contener el atributo.
 */
public record AttributeRequestDTO(
    @NotBlank String key,
    @NotNull AttributeType type,
    Boolean isRequired,
    Boolean imgSelector,
    List<Object> availableValues // Lista opcional de valores permitidos
) {
}
