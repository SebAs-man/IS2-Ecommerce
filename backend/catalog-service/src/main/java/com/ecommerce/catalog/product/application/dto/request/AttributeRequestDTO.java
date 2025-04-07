package com.ecommerce.catalog.product.application.dto.request;

import com.ecommerce.catalog.product.domain.constant.AttributeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

/**
 * Objeto de transferencia de datos (DTO) para representar una solicitud de atributo.
 * Esta clase encapsula los datos necesarios para definir o solicitar un atributo,
 * incluyendo metadatos como tipo, clave y restricciones opcionales.
 *
 * @param key Identificador único del atributo. Debe ser una cadena que no esté en blanco.
 * @param type El tipo de atributo, representado por la enumeración {@code AttributeType}.
 * No puede ser nulo y determina el formato o la validación de datos para el valor del atributo.
 * @param label La etiqueta que se mostrará al cliente para representar el atributo.
 * @param isVariantOption Bandera que indica si el atributo es obligatorio.
 * Si es true, el atributo debe tener un valor.
 * @param defaultValue Valor por defecto del atributo (si aplica)
 * Esto permite restringir los valores permitidos que puede contener el atributo.
 */
public record AttributeRequestDTO(
    @NotBlank(message = "Key cannot be blank.")
    @Size(max = 20, message = "Key must be less than 20 characters.")
    String key,

    @NotBlank(message = "Label cannot be blank.")
    @Size(max = 20, message = "Label must be less than 20 characters.")
    String label,

    @NotNull(message = "Type cannot be null.")
    AttributeType type,
    
    Boolean isVariantOption,
    Boolean isRequired,
    Object defaultValue
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
