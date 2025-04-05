package com.ecommerce.catalog.product.domain.constant;

/**
 * Define los tipos de datos posibles para los valores de los atributos.
 * Usado en Attribute para validación e interpretación.
 */
public enum AttributeType {
    STRING,
    INTEGER,
    DOUBLE,
    BOOLEAN,
    COLOR_HEX,   // Valida formato #RRGGBB o #RGB
    LIST_STRING;
    // ...otros si son necesarios...
}
