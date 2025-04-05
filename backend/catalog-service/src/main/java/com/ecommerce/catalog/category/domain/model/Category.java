package com.ecommerce.catalog.category.domain.model;

import com.ecommerce.catalog.sharedkernel.domain.model.BaseEntity;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad que representa una Categoría en la jerarquía del catálogo (Array of Ancestors).
 * El nombre debe ser único (ignorando mayúsculas/minúsculas).
 */
@Document(collection = "categories")
public class Category extends BaseEntity<String> {
    @Serial
    private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    @Indexed(unique = true, collation = "{'locale': 'es', 'strength': 2}")
    private NonBlankString name;
    private String description;
    // --- Atributos referentes a otros documentos ---
    @Indexed private String parentId;
    @Indexed private List<String> ancestors;

    /**
     * Constructor por defecto de la clase Category.
     * Inicializa una nueva instancia de la entidad Category sin valores por defecto.
    */
    public Category() { super(); }

    /**
     * Constructor que inicializa una nueva instancia de la entidad Categoría con los parámetros dados.
     *
     * @param name El nombre único de la categoría, representado como una NonBlankString.
     * @param description La descripción de la categoría, que es opcional y puede ser una cadena vacía si no se proporciona.
     * @param parentId El identificador único de la categoría padre en la jerarquía, o null si esta categoría es una categoría raíz.
     * @param ancestors La lista de ID de categorías ancestrales, representando la ruta jerárquica hasta esta categoría.
     */
    public Category(String name, String description, String parentId, List<String> ancestors) {
        super();
        setName(name);
        setDescription(description);
        setParentId(parentId);
        setAncestors(ancestors);
    }

    // --- Getters ---

    public NonBlankString getName() { return name; }
    public String getDescription() { return description; }
    public String getParentId() { return parentId; }
    public List<String> getAncestors() { return Collections.unmodifiableList(ancestors); }

    // --- Setters ---

    public void setName(String name) { this.name = new NonBlankString(name); }
    public void setDescription(String description) { this.description = description == null || description.isBlank() ? null : description.trim(); }
    public void setParentId(String parentId) { this.parentId = parentId == null || parentId.isBlank() ? null : parentId.trim(); }
    public void setAncestors(List<String> ancestors) {
        this.ancestors = ancestors == null ? Collections.emptyList() : List.copyOf(ancestors);
        if(!ancestors.contains(getId())){
            this.ancestors.add(getId());
        }
    }
}
