package com.ecommerce.catalog.product.domain.model.vo;

import com.ecommerce.catalog.product.domain.constant.AttributeType;
import com.ecommerce.libs.domain.vo.NonBlankString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un atributo utilizado para definir opciones de variantes.
 * Es un contenedor para la clave del atributo, el tipo de datos esperado, la obligatoriedad,
 * si afecta a las imágenes y los valores disponibles en caso de restricciones.
 */
public final class Attribute implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    private final NonBlankString key;
    private final NonBlankString label;
    private final AttributeType type;
    private final Object defaultValue;
    /**
     * Según su combinación, difiere el significado del atributo:
     * {@code (true, true)}: El usuario debe seleccionar un valor para este atributo (ej.: Talla).
     * {@code (true, false)}: Es un atributo de opción opcional, valga la redundancia.
     * El usuario puede o no seleccionarlo. Si no lo selecciona,
     * podría o no aplicarse un defaultValue al leer.
     * {@code (false, true)}: Es un atributo descriptivo/base.
     * Todas las variantes deben tener un valor específico (sea el defaultValue o un override).
     * {@code (false, false)}: Es un atributo descriptivo/base.
     * Las variantes pueden o no tener un valor específico.
     */
    private final Boolean isVariantOption;
    private final Boolean isRequired;

    /**
     * Constructor para la definición de una opción de variante.
     * @param key La clave como String.
     * @param type El tipo de dato esperado.
     * @param isVariantOption Juega un papel según {@code isRequired}
     * @param isRequired Juega un papel según {@code isVariantOption}
     * @param defaultValue Valor por defecto del atributo (si aplica)
     */
    public Attribute(NonBlankString key, NonBlankString label, AttributeType type,
                     Boolean isVariantOption, Boolean isRequired, Object defaultValue) {
        this.key =key;
        this.label = label;
        this.type = Objects.requireNonNull(type, "type is required");
        this.isVariantOption = isVariantOption != null && isVariantOption;
        this.isRequired = isRequired != null && isRequired;
        if(defaultValue != null){
            try{
                this.type.validate(defaultValue);
            } catch (Exception e){
                throw new IllegalArgumentException(
                        "Default value is not valid " + defaultValue + "for type " +
                        this.type + ". Error is in key: " + this.key.value(), e);
            }
        }
        this.defaultValue = defaultValue;
    }

    // --- Getters ---

    public NonBlankString getKey() { return key; }
    public NonBlankString getLabel() { return label; }
    public AttributeType getType() { return type; }
    public Boolean getIsVariantOption() { return isVariantOption; }
    public Boolean getIsRequired() { return isRequired; }
    public Object getDefaultValue() { return defaultValue; }

    // --- Métodos heredados ---

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Attribute that = (Attribute) obj;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "VariantOptionDefinition{" +
                "attributeKey='" + key +
                ", type=" + type +
                ", isVariantOption=" + isVariantOption +
                ", isRequired=" + isRequired +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
