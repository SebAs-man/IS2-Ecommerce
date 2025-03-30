package com.ecommerce.catalog.product.Domain.model;

import com.ecommerce.catalog.product.Domain.constant.AttributeKey;
import com.ecommerce.catalog.product.Domain.constant.AttributeType;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Modelo inmutable que representa un atributo adicional del modelo Producto.
 */
public final class Attribute {
    private final AttributeKey key; //clave normalizada
    private final AttributeType type;
    private final Object value; //tipo de dato esperado

    /**
     * Constructor privado para no crear objetos de este tipo sin usar el
     * patrón de diseño static factory method.
     * @param key clave del atributo a crear.
     * @param type tipo del valor del atributo a crear.
     * @param value datos o valores del atributo a crear.
     */
    private Attribute(AttributeKey key, AttributeType type, Object value) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    /**
     * El ÚNICO punto de entrada para crear instancias de Attribute desde fuera de la clase.
     * Responsable de validar, normalizar y luego llamar al constructor privado.
     * @param key clave del atributo a crear.
     * @param value datos o valores del atributo a crear.
     * @return la instancia creada o una excepción en caso de no pasar las validaciones.
     */
    public static Attribute create(AttributeKey key, Object value) {
        Objects.requireNonNull(key, "Key cannot be null...");
        Objects.requireNonNull(value, "Value cannot be null...");

        /**
        String normalizedKey = key.toString();
        System.out.println(normalizedKey);
         */
        AttributeType type = key.getCanonicalType();
        if(!isValidValueForType(value, type)){
            throw new IllegalArgumentException("Value '" + value + "' is not valid for type '" + type.toString() + "'" + "for key '" + key + "'.");
        }
        return new Attribute(key, type, value);
    }

    /**
     * Verifica que los tipos de datos ofrecidos en el atributo tengan correlación
     * con los tipos de datos predeterminados.
     * @param value datos o valores del atributo.
     * @param type tipo de dato de los valores del atributo.
     * @return true si se cumplen los tipos de datos
     *          false si no hay relación entre los tipos de datos.
     */
    private static boolean isValidValueForType(Object value, AttributeType type) {
        return switch (type){
            case STRING -> value instanceof String && !((String) value).isBlank();
            case INTEGER -> value instanceof Integer;
            case DOUBLE -> value instanceof Double;
            case BOOLEAN -> value instanceof Boolean;
            case LIST_STRING -> value instanceof List &&
                    ((List<?>) value).stream()
                            .allMatch(item -> item instanceof String && !((String) item).isBlank());
            default -> false;
        };
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Attribute attribute = (Attribute)obj;
        return key.equals(attribute.key) &&
                type == attribute.type &&
                Objects.equals(value, attribute.value);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "key='" + key + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(key,type,value);
    }

    /**
     * Obtener la clave del atributo
     * @return un cadena de caracteres.
     */
    public AttributeKey getKey() {
        return key;
    }

    /**
     * Obtener el tipo de dato almacenado en el atributo.
     * @return el tipo de dato predeterminado.
     */
    public AttributeType getType() {
        return type;
    }

    /**
     * Obtener el valor o dato definido en el atributo.
     * @return el valor del atributo.
     */
    public Object getValue() {
        return value;
    }
}
