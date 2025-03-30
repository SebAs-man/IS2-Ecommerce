package com.ecommerce.catalog.product.Domain.constant;

import static com.ecommerce.catalog.product.Domain.constant.AttributeType.*;

/**
 * Listado de claves válidas en la base de datos para los atributos adicionales del producto.
 */
public enum AttributeKey {
    COLOR(STRING),
    SIZE(DOUBLE),
    MATERIAL(STRING),
    BRAND(STRING);

    //Asocia cada clave con su tipo canónico
    private final AttributeType canonicalType;

    /**
     * Tipo canónico de las claves
     * Permite limitar el uso de las claves con su respectivo tipo de dato.
     * @param canonicalType tipo canónico de la clave.
     */
    AttributeKey(AttributeType canonicalType) {
        this.canonicalType = canonicalType;
    }

    /**
     * Obtiene el tipo canónico de la clave.
     * @return el tipo canónico de la clave
     */
    public AttributeType getCanonicalType() {
        return canonicalType;
    }
}
