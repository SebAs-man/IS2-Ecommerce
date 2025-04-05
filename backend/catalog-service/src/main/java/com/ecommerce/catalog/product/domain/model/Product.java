package com.ecommerce.catalog.product.domain.model;

import com.ecommerce.catalog.product.domain.model.vo.Attribute;
import com.ecommerce.catalog.sharedkernel.domain.model.BaseEntity;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Entidad Producto: Información base, atributos estáticos y definición de opciones de variante.
 */
@Document(collection = "products")
public class Product extends BaseEntity<String> {
    @Serial
    private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    private NonBlankString name;
    private String description;
    // --- Atributos referentes a otros documentos ---
    @Indexed private String brandId;
    @Indexed private List<NonBlankString> categoriesId;
    // --- Atributos adicionales del producto ---
    private Map<NonBlankString, NonBlankString> staticAttributes; // Estáticos/Descriptivos (clave-valor simples)
    private List<Attribute> dynamicAttributes; // Cuyas opciones generan las variantes

    /**
     * Constructor por defecto para la entidad Product.
     */
    public Product() {
        super();
        this.description = "";
        this.brandId = "";
        this.categoriesId = Collections.emptyList();
        this.staticAttributes = Collections.emptyMap();
        this.dynamicAttributes = Collections.emptyList();
    }

    // --- Métodos funcionales ---

    /**
     * Añade un atributo estático al mapa de atributos estáticos del producto.
     * Las cadenas de clave y valor proporcionadas deben estar en blanco y se normalizan antes de ser añadidas.
     * @param key la clave del atributo, no debe ser nula ni estar en blanco
     * @param value el valor del atributo, no debe ser nulo ni estar en blanco
     */
    public void putStaticAttribute(String key, String value) {
        NonBlankString availableKey = new NonBlankString(key);
        NonBlankString availableValue = new NonBlankString(value);
        this.staticAttributes.put(availableKey, availableValue);
    }

    /**
     * Elimina un atributo estático del mapa de atributos estáticos del producto.
     * La clave proporcionada se valida para garantizar que no es nula o está vacía antes de intentar eliminarla.
     * @param key la clave del atributo estático a eliminar; no debe ser nula ni estar en blanco.
     */
    public void removeStaticAttribute(String key) {
        NonBlankString availableKey = new NonBlankString(key);
        if(this.staticAttributes.remove(availableKey) != null){
        }
    }

    /**
     * Asigna el producto a una categoría específica añadiendo el ID de categoría proporcionado
     * a la lista de ID de categoría asociados si no está ya presente.
     * @param categoryId el ID de la categoría a la que asignar el producto; no debe ser nulo ni estar en blanco.
     */
    public void assignToCategory(String categoryId) {
        NonBlankString availableCategoryId = new NonBlankString(categoryId);
         if (!this.categoriesId.contains(availableCategoryId)) {
             this.categoriesId.add(availableCategoryId);
         }
    }

    /**
     * Elimina la categoría especificada de la lista de identificadores de categoría asignados al producto.
     * El ID de categoría introducido se valida para asegurarse de que no es nulo o está en blanco antes de intentar la eliminación.
     * @param categoryId el ID de la categoría a eliminar; no debe ser nulo ni estar en blanco.
     */
    public void removeFromCategory(String categoryId) {
        NonBlankString availableCategoryId = new NonBlankString(categoryId);
        if(this.categoriesId.remove(availableCategoryId)){
        }
    }

    // --- Getters ---

    public String getName() { return name.value(); }
    public String getDescription() { return description; }
    public String getBrandId() { return brandId; }
    public List<NonBlankString> getCategoryId() { return Collections.unmodifiableList(categoriesId); }
    public Map<NonBlankString, NonBlankString> getStaticAttributes() { return Collections.unmodifiableMap(staticAttributes); }
    public List<Attribute> getDynamicAttributes() { return Collections.unmodifiableList(dynamicAttributes); }

    // --- Setters ---

    public void setName(String name) { this.name = new NonBlankString(name); }
    public void setDescription(String description) { this.description = description == null ? "" : description; }
    public void setBrandId(String brandId) { this.brandId = brandId == null ? "" : brandId; }
    public void setCategoryId(List<NonBlankString> categoryId) { this.categoriesId = categoryId == null ? Collections.emptyList() : List.copyOf(categoryId); }
    public void setStaticAttributes(Map<NonBlankString, NonBlankString> staticAttributes) { this.staticAttributes = staticAttributes == null ? Collections.emptyMap() : Map.copyOf(staticAttributes); }
    public void setDynamicAttributes(List<Attribute> dynamicAttributes) { this.dynamicAttributes = dynamicAttributes == null ? Collections.emptyList() : List.copyOf(dynamicAttributes); }
}
