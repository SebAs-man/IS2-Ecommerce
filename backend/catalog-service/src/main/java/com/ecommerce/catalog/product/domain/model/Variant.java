package com.ecommerce.catalog.product.domain.model;

import com.ecommerce.catalog.sharedkernel.domain.model.BaseEntity;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Entidad que representa una variante específica y vendible de un producto.
 */
@Document(collection = "variants")
public class Variant extends BaseEntity<String> {
    @Serial private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    @Indexed private String productId; // Id del producto padre.
    @Indexed(unique = true) private String sku;
    private BigDecimal price;
    private Integer stock;
    private Boolean available; // Calculado desde stock.
    private List<String> images;
    private Map<String, Object> attributes;
    // --- Atributos adicionales ---
    @Version private Long version; // Bloqueo optimista

    /**
     * Constructor por defecto para la entidad Variant.
     */
    public Variant() {
        super();
        this.stock = 0;
        this.available = true;
        this.images = Collections.emptyList();
        this.attributes = Collections.emptyMap();
    }

    // --- Getters ---

    public String getProductId() { return productId; }
    public String getSku() { return sku; }
    public BigDecimal getPrice() { return price; }
    public Integer getStock() { return stock; }
    public Boolean getAvailable() { return available; }
    public List<String> getImages() { return Collections.unmodifiableList(images); }
    public Map<String, Object> getAttributes() { return Collections.unmodifiableMap(attributes); }
    public Long getVersion() { return version; }

    // --- Setters ---

    public void setProductId(String productId) { this.productId = productId; }
    public void setSku(String sku) { this.sku = sku; }
    public void setPrice(BigDecimal price) {
        Objects.requireNonNull(price, "Price cannot be null");
        if(price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
    public void setStock(Integer stock) { this.stock = stock == null || stock < 0 ? 0 : stock; }
    public void setAvailable(Boolean available) { this.available = available == null || available; }
    public void setImages(List<String> images) { this.images = images == null ? Collections.emptyList() : List.copyOf(images); }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes == null ? Collections.emptyMap() : Map.copyOf(attributes); }
}
