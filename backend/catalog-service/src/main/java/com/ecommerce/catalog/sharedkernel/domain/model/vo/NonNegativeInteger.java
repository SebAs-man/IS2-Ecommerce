package com.ecommerce.catalog.sharedkernel.domain.model.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un valor entero no negativo.
 * Esta clase garantiza que el valor encapsulado siempre sea mayor o igual a cero.
 * Está diseñada para aplicar y representar restricciones de dominio para valores que no deben ser nulos ni negativos.
 * Las instancias de este registro son inmutables, lo que garantiza la seguridad de los subprocesos y la consistencia en su uso.
 * @param value el valor entero encapsulado por este registro; debe ser distinto de nulo y mayor o igual a cero.
 */
public record NonNegativeInteger(Integer value) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construye un objeto NonNegativeInteger tras validar que el valor de entrada no sea nulo ni negativo.
     * @param value: el valor entero que se encapsulará; debe ser distinto de nulo y mayor o igual a cero.
     */
    public NonNegativeInteger {
        Objects.requireNonNull(value, "Value cannot be null.");
        if(value < 0){
            throw new IllegalArgumentException("Value cannot be negative.");
        }
    }
}
