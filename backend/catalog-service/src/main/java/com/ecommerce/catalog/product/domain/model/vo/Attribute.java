package com.ecommerce.catalog.product.domain.model.vo;

import com.ecommerce.catalog.product.domain.constant.AttributeType;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa un atributo utilizado para definir opciones de variantes.
 * Es un contenedor para la clave del atributo, el tipo de datos esperado, la obligatoriedad,
 * si afecta a las imágenes y los valores disponibles en caso de restricciones.
 */
public class Attribute implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // --- Atributos básicos ---
    private final NonBlankString key; // Clave String normalizada
    private final AttributeType valueType; // TIPO esperado para los valores
    private final Boolean isRequired;  // ¿Obligatorio para formar una variante?
    private final Boolean imgSelector; // ¿Seleccionar esta opción cambia las imágenes?
    private final List<Object> availableValues; // Valores específicos permitidos.  Vacía si no hay restricción.

    /**
     * Constructor para la definición de una opción de variante.
     * @param key La clave como String.
     * @param valueType El tipo de dato esperado.
     * @param isRequired Si la opción es obligatoria.
     * @param imgSelector Sí afecta a las imágenes.
     * @param availableValues Sí hay restricciones en los valores permitidos.
     */
    public Attribute(String key, AttributeType valueType, Boolean isRequired,
                     Boolean imgSelector, List<Object> availableValues) {
        this.key = new NonBlankString(key); // Normalización
        this.valueType = Objects.requireNonNull(valueType, "valueType is required");
        this.isRequired = isRequired != null && isRequired;
        this.imgSelector = imgSelector != null && imgSelector;
        this.availableValues =  availableValues == null ? Collections.emptyList() : List.copyOf(availableValues);
    }

    // --- Métodos heredados ---

    @Override
    public String toString() {
        return "VariantOptionDefinition{" +
                "attributeKey='" + key +
                ", valueType=" + valueType +
                ", isRequired=" + isRequired +
                ", imageSelector=" + imgSelector +
                ", availableValues=" + availableValues +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, valueType, isRequired, imgSelector, availableValues);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Attribute that = (Attribute) obj;
        return isRequired == that.isRequired &&
                imgSelector == that.imgSelector &&
                key.equals(that.key) &&
                valueType == that.valueType &&
                Objects.equals(availableValues, that.availableValues);
    }


    // --- Getters ---

    public String getKey() { return key.value(); }
    public AttributeType getValueType() { return valueType; }
    public Boolean getIsRequired() { return isRequired; }
    public Boolean getImgSelector() { return imgSelector; }
    public List<Object> getAvailableValues() { return availableValues; }
}
