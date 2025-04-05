package com.ecommerce.ShoppingCart.cart.domain.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa un item en un carrito de compras
 * Esta clase encapsula todas las propiedades y el comportamiento
 * asociados con un solo artículo dentro del carrito.
 * Implementa la interfaz Serializable para permitir
 * serializar el objeto para su almacenamiento o transmisión.
 */
public class CartItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serializador predefinido para los objetos.

    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private String productName;
    private String imageUrl;
    private final List<>

    /**
     * Constructor por defecto.
     * Crea una instancia de CartItem con todos los campos inicializados en valores predeterminados.
     * Ideal para usar cuando se desea configurar los valores posteriormente de forma manual.
     */
    public CartItem() {
    }

    /**
     * Constructor con todos los argumentos.
     * Permite crear una instancia de CartItem inicializando todos sus campos.
     *
     * @param productId    El identificador único del producto.
     * @param quantity     La cantidad del producto en el carrito.
     * @param price        El precio del producto.
     * @param productName  El nombre del producto.
     * @param imageUrl     La URL de la imagen del producto.
     */
    public CartItem(String productId, Integer quantity, BigDecimal price, String productName, String imageUrl) {
        setProductId(productId);
        setPrice(price);
        setQuantity(quantity);
        setProductName(productName);
        this.imageUrl = imageUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        CartItem other = (CartItem) obj;
        return Objects.equals(productId, other.productId);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", productName='" + productName + '\'' +
                '}';
    }

    /**
     * Recibe el identificador único del producto
     * @return el identificador como String
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Establece el identificador único del producto.
     * @param productId el identificador del producto como una cadena de caracteres
     */
    public void setProductId(String productId) {
        if(productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be null or blank");
        }
        this.productId = productId;
    }

    /**
     * Obtiene el precio del producto.
     * @return un decimal con el precio
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Modifica el precio del producto.
     * @param price el nuevo precio.
     */
    public void setPrice(BigDecimal price) {
        Objects.requireNonNull(price, "price must not be null");
        if(price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price must be greater than zero");
        }
        this.price = price;
    }

    /**
     * Obtiene la cantidad del producto en inventario.
     * @return un número entero positivo
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Modifica la cantidad en inventario del producto.
     * @param quantity la nueva cantidad.
     */
    public void setQuantity(Integer quantity) {
        if(quantity != null) {
            if(quantity >= 0) {
                this.quantity = quantity;
                return;
            }
            throw new IllegalArgumentException("quantity must be greater than or equal to zero");
        }
        this.quantity = 0;
    }

    /**
     * Obtiene el nombre del producto.
     * @return una cadena de caracteres.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Modifica el nombre del producto
     * @param productName el nuevo nombre
     */
    public void setProductName(String productName) {
        if(productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be null or blank");
        }
        this.productName = productName;
    }

    /**
     * Obtiene la URL de la imagen del producto.
     * @return un String con la URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Cambia la URL de la imagen del producto.
     * @param imageUrl la nueva URL
     */
    public void setImageUrl(String imageUrl) {
        if(imageUrl != null && imageUrl.isBlank()) {
            throw new IllegalArgumentException("imageUrl must not be blank");
        }
        this.imageUrl = imageUrl;
    }
}
