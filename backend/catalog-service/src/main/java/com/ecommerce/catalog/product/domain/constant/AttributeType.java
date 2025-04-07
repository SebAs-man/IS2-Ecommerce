package com.ecommerce.catalog.product.domain.constant;


import java.util.List;
import java.util.Objects;

/**
 * Enumeración que define diferentes tipos de atributos y su mecanismo de validación respectiva.
 * Cada tipo de atributo especifica reglas de validación personalizadas para garantizar que los datos
 * sean del tipo y formato esperados.
 * Los tipos de atributos incluyen:
 * - STRING: Representa cadenas no en blanco.
 * - INTEGER: Representa números enteros.
 * - DOUBLE: Representa números de punto flotante.
 * - BOOLEAN: Representa valores booleanos.
 * - COLOR_HEX: Representa cadenas con formato válido de color hexadecimal.
 * - LIST_STRING: Representa listas que contienen únicamente cadenas de texto.
 * Los métodos específicos de cada tipo de atributo permiten la validación directa de datos
 * para asegurar que cumplen las restricciones establecidas.
 */
public enum AttributeType {
    STRING{
        @Override
        public void validate(Object value) {
            Objects.requireNonNull(value, "Value cannot be null for String type.");
            if(!(value instanceof String) || ((String) value).isBlank()){
                throw new IllegalArgumentException("Value must be a String not blank for String type.");
            }
        }
    },
    INTEGER{
        @Override
        public void validate(Object value) {
            Objects.requireNonNull(value, "Value cannot be null for Integer type.");
            if(!(value instanceof Integer)){
                throw new IllegalArgumentException("Value must be an Integer for Integer type.");
            }
        }
    },
    DOUBLE{
        @Override
        public void validate(Object value) {
            Objects.requireNonNull(value, "Value cannot be null for Double type.");
            if(!(value instanceof Double)){
                throw new IllegalArgumentException("Value must be a Double for Double type.");
            }
        }
    },
    BOOLEAN{
        @Override
        public void validate(Object value) {
            Objects.requireNonNull(value, "Value cannot be null for Boolean type.");
            if(!(value instanceof Boolean)){
                throw new IllegalArgumentException("Value must be a Boolean for Boolean type.");
            }
        }
    },
    COLOR_HEX{
        @Override
        public void validate(Object value) {
            Objects.requireNonNull(value, "Value cannot be null for ColorHex type.");
            if(!(value instanceof String hexColor)){
                throw new IllegalArgumentException("Value must be a String for ColorHex type.");
            }
            if(!hexColor.matches("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")){
                throw new IllegalArgumentException("Value must be a valid Hex Color.");
            }
        }
    },
    LIST_STRING{
        @Override
        public void validate(Object value) {
            Objects.requireNonNull(value, "Value cannot be null for ListString type.");
            if(!(value instanceof List)){
                throw new IllegalArgumentException("Value must be a List for ListString type.");
            }
            if(((List<?>) value).stream().allMatch(
                    item -> item instanceof String)){
                throw new IllegalArgumentException("Value must be a List of Strings for ListString type.");
            }
        }
    };
    // ...otros si son necesarios...

    /**
     * Valida si el objeto 'value' proporcionado es compatible con este tipo de atributo.
     * @param value El valor a validar (no debe ser null).
     * @throws NullPointerException si value es null.
     * @throws IllegalArgumentException si el valor no es del tipo esperado o no cumple las reglas del formato.
     */
    public abstract void validate(Object value);
}
