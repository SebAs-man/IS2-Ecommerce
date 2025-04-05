package com.ecommerce.catalog.sharedkernel.domain.util;

import com.ecommerce.catalog.product.domain.constant.AttributeType;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase de utilidad para validar valores contra tipos de atributos específicos.
 * Esta clase proporciona un conjunto de reglas de validación para asegurar que el valor de entrada coincide con el tipo de atributo esperado.
 * El tipo de atributo esperado. La validación se realiza basándose en tipos de atributos predefinidos
 * como STRING, INTEGER, DOUBLE, BOOLEAN, COLOR_HEX y LIST_STRING.
 * La lógica de validación comprueba la compatibilidad de tipos, el cumplimiento de formatos (por ejemplo, formato de color HEX),
 * y otras condiciones específicas (por ejemplo, cadenas no vacías en listas).
 * Esta clase no está pensada para ser instanciada y solo proporciona métodos estáticos.
 */
public class AttributeValueValidator {
    // Patrón para colores HEX (lo movemos aquí desde Attribute)
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");

    /**
     * Clase de utilidad para validar valores de atributos basándose en sus tipos especificados.
     * Esta clase proporciona métodos estáticos para asegurar que los valores proporcionados se ajustan a las definiciones de tipo de atributo esperadas.
     * Definiciones de tipo de atributo esperadas. No se pueden crear instancias de esta clase.
     */
    private AttributeValueValidator() {}

    /**
     * Valida si el valor dado coincide con el tipo de atributo especificado.
     * @param value el valor del objeto a validar. No debe ser nulo.
     * @param type el tipo de atributo esperado que debe coincidir con el valor. No debe ser nulo.
     * @return  {@code true} si el valor es válido para el tipo especificado; en caso contrario, se lanza una excepción para los casos no válidos.
     */
    public static boolean isValid(Object value, AttributeType type) {
        Objects.requireNonNull(value, "El valor a validar no puede ser nulo.");
        Objects.requireNonNull(type, "El tipo esperado no puede ser nulo.");

        return switch (type) {
            case STRING -> {
                if (!(value instanceof String)) throw new IllegalArgumentException("Tipo inválido, se esperaba String.");
                if (((String) value).isBlank()) throw new IllegalArgumentException("Valor String no puede estar en blanco.");
                yield true;
            }
            case INTEGER -> {
                if (!(value instanceof Integer)) throw new IllegalArgumentException("Tipo inválido, se esperaba Integer.");
                yield true;
            }
            case DOUBLE -> {
                if (!(value instanceof Double)) throw new IllegalArgumentException("Tipo inválido, se esperaba Double.");
                yield true;
            }
            case BOOLEAN -> {
                if (!(value instanceof Boolean)) throw new IllegalArgumentException("Tipo inválido, se esperaba Boolean.");
                yield true;
            }
            case COLOR_HEX -> {
                if (!(value instanceof String)) throw new IllegalArgumentException("Tipo inválido para COLOR_HEX, se esperaba String.");
                if (!HEX_COLOR_PATTERN.matcher((String) value).matches()) throw new IllegalArgumentException("Formato de color HEX inválido: " + value);
                yield true;
            }
            case LIST_STRING -> {
                if (!(value instanceof List)) throw new IllegalArgumentException("Tipo inválido para LIST_STRING, se esperaba List.");
                for (Object item : (List<?>) value) {
                    if (!(item instanceof String) || ((String) item).isBlank()) {
                        throw new IllegalArgumentException("LIST_STRING contiene elementos inválidos (no String o en blanco): " + item);
                    }
                }
                yield true;
            }
            default -> throw new IllegalArgumentException("Tipo de atributo no soportado: " + type);
        };
    }

    /**
     * Verifica si un valor está contenido dentro de una lista de valores permitidos,
     * considerando el tipo esperado para una comparación adecuada.
     * Asume que isValid(amount, type) ya fue llamado y es verdadero.
     * @param value El valor a verificar.
     * @param allowedValues La lista de valores permitidos (puede ser nula o vacía).
     * @param type El tipo esperado de los valores.
     * @return true si allowedValues es nula/vacía o si amount se encuentra en la lista, false en caso contrario.
     */
    public static boolean isValueInList(Object value, List<?> allowedValues, AttributeType type) {
        if (allowedValues == null || allowedValues.isEmpty()) {
            return true; // Si no hay lista de permitidos, cualquier valor del tipo correcto es válido.
        }
        for (Object allowed : allowedValues) {
            // Primero, ¿son del mismo tipo básico esperado?
            if (value.getClass() == allowed.getClass()) {
                if (Objects.equals(value, allowed)) {
                    return true;
                }
            }
        }
        return false; // No se encontró en la lista de permitidos
    }
}
