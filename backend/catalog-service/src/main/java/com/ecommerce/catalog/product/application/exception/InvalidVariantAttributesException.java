package com.ecommerce.catalog.product.application.exception;

/**
 * Excepción ocurrida si los atributos de una variable no coinciden con los definidos del producto
 */
public class InvalidVariantAttributesException extends RuntimeException {
    public InvalidVariantAttributesException(String message) {
        super(message);
    }
}
