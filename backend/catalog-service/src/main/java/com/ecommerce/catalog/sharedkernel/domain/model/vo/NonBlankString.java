package com.ecommerce.catalog.sharedkernel.domain.model.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un valor de cadena que se garantiza que no está en blanco.
 * Una cadena que no está en blanco se define como una cadena que no es nula ni está compuesta en su totalidad por espacios en blanco.
 * Este registro es inmutable y seguro debido al uso de campos finales y a la inmutabilidad del tipo String.
 * @param value el valor de la cadena
 */
public record NonBlankString(String value) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construye una instancia de NonBlankString y normaliza el valor de paso.
     * @param value el valor de la cadena
     */
    public NonBlankString {
        Objects.requireNonNull(value, "Value cannot be null.");
        if(value.isBlank()){
            throw new IllegalArgumentException("Value cannot be blank.");
        }
        value = value.trim();
    }
}
