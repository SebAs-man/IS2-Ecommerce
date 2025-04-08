package com.ecommerce.catalog.product.domain.model;

import com.ecommerce.catalog.product.domain.model.vo.Attribute;
import com.ecommerce.catalog.sharedkernel.domain.model.BaseEntity;
import com.ecommerce.libs.domain.vo.NonBlankString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.util.*;

/**
 * Entidad Producto: Información base, atributos estáticos y definición de opciones de variante.
 */
@Document(collection = "products")
public class Product extends BaseEntity<String> {
    @Serial
    private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    @Indexed(collation = "{'locale': 'es', 'strength': 2}")
    private NonBlankString name;
    private String description;
    private List<Attribute> attributeDefinitions; // Cuyas opciones generan las variantes
    // --- Atributos referentes a otros documentos ---
    @Indexed private NonBlankString brandId;
    @Indexed private List<String> categoriesId;

    /**
     * Constructor por defecto para la entidad Product.
     */
    public Product() { super(); }

    /**
     * Construye un nuevo producto con los detalles especificados.
     * @param id el identificador único del producto; no debe ser nulo.
     * @param name el nombre del producto; no debe ser nulo ni estar en blanco.
     * @param description la descripción del producto; si es nulo, se asignará una cadena vacía.
     * @param brandId el identificador de la marca asociada al producto; si es nulo, se asignará una cadena vacía.
     * @param categoriesId lista de categorías a las que pertenece el producto.
     * @param attributeDefinitions lista de atributos adicionales predefinidos del producto.
     */
    public Product(String id, String name, String description, String brandId,
                   List<String> categoriesId, List<Attribute> attributeDefinitions) {
        super(id);
        setName(name);
        setDescription(description);
        setBrandId(brandId);
        setCategoriesId(categoriesId);
        setAttributeDefinitions(attributeDefinitions);
    }

    // --- Getters ---

    public NonBlankString getName() { return name; }
    public String getDescription() { return description; }
    public NonBlankString getBrandId() { return brandId; }
    public List<Attribute> getAttributeDefinitions() { return Collections.unmodifiableList(attributeDefinitions); }
    public List<String> getCategoriesId() { return Collections.unmodifiableList(categoriesId); }

    // --- Setters ---

    public void setName(String name) { this.name = new NonBlankString(name); }
    public void setDescription(String description) { this.description = description == null || description.isBlank() ? null : description.trim(); }
    public void setBrandId(String brandId) { this.brandId = brandId == null || brandId.isBlank() ? null : new NonBlankString(brandId); }
    public void setCategoriesId(List<String> categoriesId) { this.categoriesId = categoriesId == null ? new ArrayList<>(): new ArrayList<>(categoriesId); }
    // Por su complejidad, se declaran protegidos para que no puedan ser modificados por el servicio
    protected void setAttributeDefinitions(List<Attribute> attributeDefinitions) { this.attributeDefinitions = attributeDefinitions == null ? Collections.emptyList() : List.copyOf(attributeDefinitions); }

    // --- Métodos funcionales no son declarados para simplificar la implementación ---
    // ej. public void addCategory; public void addStaticAttribute; etc.
}
