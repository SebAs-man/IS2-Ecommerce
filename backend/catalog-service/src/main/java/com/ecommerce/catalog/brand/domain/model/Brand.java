package com.ecommerce.catalog.brand.domain.model;

import com.ecommerce.catalog.sharedkernel.domain.model.BaseEntity;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;

/**
 * Entidad que representa una Marca de productos.
 */
@Document(collection = "brands")
public class Brand extends BaseEntity<String> {
    @Serial
    private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    @Indexed(unique = true, collation = "{'locale: 'es', 'strength: 2}")
    private NonBlankString name;
    private String description;
    private String logoUrl;

    /**
     * Constructor de una nueva marca, sin valores por defecto.
     */
    public Brand() { super(); }

    /**
     * Construye un nuevo objeto Brand con los parámetros especificados.
     *
     * @param name el nombre de la marca, debe estar en blanco
     * @param description una breve descripción de la marca, puede estar vacía pero no ser nula
     * @param logoUrl la URL del logo de la marca, puede estar vacía pero no ser nula
     */
    public Brand(String name, String description, String logoUrl) {
        super();
        setName(name);
        setDescription(description);
        setLogoUrl(logoUrl);
    }

    // --- Getters ---

    public NonBlankString getName() { return name; }
    public String getDescription() { return description; }
    public String getLogoUrl() { return logoUrl; }

    // --- Setters ---

    public void setName(String name) { this.name = new NonBlankString(name); }
    public void setDescription(String description) { this.description = description == null ? null : description.trim(); }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl == null ? null : logoUrl.trim(); }
}
