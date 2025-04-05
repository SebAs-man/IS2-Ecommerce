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
 * Entidad que representa una Categoría en la jerarquía (Array of Ancestors).
 */
@Document(collection = "categories")
public class Category extends BaseEntity<String> {
    @Serial private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    @Indexed(unique = true) private NonBlankString name; // nombre único de la categoría.
    private String description;
    @Indexed private String parentId; // null si es la raíz.
    // --- Atributos referentes a otros documentos ---
    @Indexed private List<String> ancestors; // Id de ancestros + el propio.

    /**
     * Constructor por defecto de la clase Category.
     * Inicializa una nueva instancia de la entidad Category sin valores por defecto.
    */
    public Category() {
        super();
        this.ancestors = Collections.emptyList();
        this.description = "";
    }

    // --- Getters ---

    public String getName() { return name.value(); }
    public String getDescription() { return description; }
    public String getParentId() { return parentId; }
    public List<String> getAncestors() { return Collections.unmodifiableList(ancestors); }

    // --- Setters ---

    public void setName(String name) { this.name = new NonBlankString(name); }
    public void setDescription(String description) { this.description = description == null ? "" : description; }
    public void setAncestors(List<String> ancestors) { this.ancestors = ancestors == null ? Collections.emptyList() : List.copyOf(ancestors); }
    public void setParentId(String parentId) { this.parentId = parentId; }
}
