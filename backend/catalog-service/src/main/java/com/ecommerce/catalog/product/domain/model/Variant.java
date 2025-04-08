package com.ecommerce.catalog.product.domain.model;

import com.ecommerce.catalog.sharedkernel.domain.model.BaseEntity;
import com.ecommerce.libs.domain.vo.Money;
import com.ecommerce.libs.domain.vo.NonBlankString;
import com.ecommerce.libs.domain.vo.NonNegativeInteger;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.util.*;

/**
 * Entidad que representa una variante específica y vendible de un producto.
 */
@Document(collection = "variants")
public class Variant extends BaseEntity<String> {
    @Serial
    private static final long serialVersionUID = 1L;
    // --- Atributos básicos ---
    @Indexed private NonBlankString productId;
    private Money price;
    private NonNegativeInteger stock;
    private Boolean available;
    private List<String> images;
    private Map<String, Object> attributes;
    // --- Atributos adicionales ---
    @Version private Long version; // Bloqueo optimista

    /**
     * Constructor por defecto para la entidad Variant.
     */
    public Variant() { super(); }

    /**
     * Constructor que inicializa una nueva instancia de una variante con parámetros predefinidos.
     * @param id identificador único de la variante
     * @param productId identificador del producto al que pertenece
     * @param price precio monetario de la variante
     * @param stock cantidad en inventario o disponibilidad de la variante
     * @param images lista de imágenes únicas de la variante
     * @param attributes atributos predefinidos por la variante, si es necesario.
     */
    public Variant(String id, String productId, Money price, Integer stock,
                   List<String> images, Map<String, Object> attributes) {
        super(id);
        setProductId(productId);
        setPrice(price);
        setStock(stock);
        setImages(images);
        setAttributes(attributes);
        setAvailable(true);
    }

    // --- Getters ---

    public NonBlankString getProductId() { return productId; }
    public Money getPrice() { return price; }
    public NonNegativeInteger getStock() { return stock; }
    public Boolean getAvailable() { return available; }
    public List<String> getImages() { return Collections.unmodifiableList(images); }
    public Map<String, Object> getAttributes() { return Collections.unmodifiableMap(attributes); }
    public Long getVersion() { return version; }

    // --- Setters ---

    public void setPrice(Money price) { this.price = Objects.requireNonNull(price, "Price cannot be null"); }
    public void setStock(Integer stock) { this.stock = new NonNegativeInteger(stock); }
    public void setAvailable(Boolean available) { this.available = available != null && available; }
    public void setImages(List<String> images) { this.images = images == null ? new ArrayList<>() : new ArrayList<>(images); }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes == null ? new HashMap<>() : new HashMap<>(attributes); }
    // Por su complejidad, se declaran protegidos para que no puedan ser modificados por el servicio.
    protected void setProductId(String productId) { this.productId = new NonBlankString(productId); }
}
