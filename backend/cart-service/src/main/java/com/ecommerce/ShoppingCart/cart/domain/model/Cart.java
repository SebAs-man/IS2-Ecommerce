package com.ecommerce.ShoppingCart.cart.domain.model;

import com.ecommerce.ShoppingCart.cart.domain.exception.ItemNotFoundInCartException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Entidad que representa el carrito de compras de un usuario.
 * Se mapea a un Hash en Redis.
 * Utiliza un ID único generado por la aplicación y guarda userId como campo indexado.
 * Implementa bloqueo optimista con @Version y TTL diferencial mediante @TimeToLive
 * cuyo valor se establece en el servicio antes de guardar.
 */
@RedisHash("carts") //keyspace en Redis será "carts: <id>"
public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id; // Identificador único del carrito de compras (generado por el servicio)

    @Indexed
    private String userId; // Identificador del usuario asociado (null si es anónimo)

    private final List<CartItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @TimeToLive(unit = TimeUnit.HOURS)
    private Long timeToLive; // Permite la eliminación automática del carrito tras un tiempo determinado en el servicio.

    @Version
    private Long version;// Campo de bloqueo optimista (se incrementa automáticamente)

    /**
     * Constructor por defecto para la clase Cart.
     * Crea una instancia vacía de Carrito con valores por defecto.
     */
    public Cart() {
        this.items = Collections.emptyList();
    }

    /**
     * Constructor principal para crear un nuevo carrito vacío.
     * El ID es generado y proporcionado por el servicio.
     * El userId se establece posteriormente si el usuario se autentica.
     * @param id El ID único generado para este carrito.
     */
    public Cart(String id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.items = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        // Los demás campos se definen en el servicio
    }

    /**
     * Añade un ítem al carrito o incrementa su cantidad si ya existe.
     * Actualiza la fecha de última modificación.
     * @param newItem El ítem a añadir (debe ser válido y tener cantidad >= 1).
     */
    public void addItem(CartItem newItem) {
        Objects.requireNonNull(newItem, "newItem must not be null");
        Optional<CartItem> existingItem = findItemById(newItem.getProductId());
        if(existingItem.isPresent()) {// Como el producto ya existe, entonces actualiza la cantidad.
            CartItem existing = existingItem.get();
            try {
                // Previene el overflow
                int newQuantity = Math.addExact(existing.getQuantity(), newItem.getQuantity());
                existing.setQuantity(newQuantity);
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("quantity overflow", e);
            }
        } else{ // El producto es nuevo
            this.items.add(newItem);
        }
        this.touch();
    }

    /**
     * Actualiza la cantidad de un ítem existente en el carrito.
     * La cantidad debe ser >= 1 (según HU-4). Usar removeItem para eliminar.
     * @param productId El ID del producto a actualizar.
     * @param newQuantity La nueva cantidad (debe ser >= 1).
     * @throws IllegalArgumentException si productId es nulo o newQuantity es < 1.
     * @throws ItemNotFoundInCartException si el producto no está en el carrito.
     */
    public void updateItem(String productId, Integer newQuantity) {
        Objects.requireNonNull(productId, "productId must not be null");
        CartItem update = findItemById(productId)
                .orElseThrow(() -> new ItemNotFoundInCartException(productId, this.id));
        update.setQuantity(newQuantity);
        this.touch();
    }

    /**
     * Elimina un ítem del carrito basado en su productId.
     * @param productId El ID del producto a eliminar.
     * @return true si el ítem fue encontrado y eliminado, false en caso contrario.
     */
    public boolean removeItem(String productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        boolean removed = this.items.removeIf(item -> item.getProductId().equals(productId));
        if(removed) {
            this.touch();
        }
        return removed;
    }

    /**
     * Elimina todos los ítems del carrito.
     */
    public void clear() {
        if(!this.items.isEmpty()) {
            this.items.clear();
            this.touch();
        }
    }

    /**
     * Calcula el número total de unidades de producto en el carrito (sumando cantidades).
     */
    public int getTotalQuantity() {
        return this.items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    /**
     * Calcula el precio total del carrito sumando (precio unitario * cantidad) de cada ítem.
     */
    public BigDecimal getTotalPrice() {
        return this.items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Actualiza la marca de tiempo de la última modificación.
     * Debe ser llamado por cualquier método que cambie el estado del carrito o sus items.
     * Alternativamente, el servicio puede llamarlo antes de guardar.
     */
    private void touch(){
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Método auxiliar para encontrar un item con el mismo id.
     * @param productId identificador del producto.
     * @return puede o no retornar el item encontrado.
     */
    private Optional<CartItem> findItemById(String productId){
        return productId == null ? Optional.empty() : this.items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        Cart other = (Cart) obj;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", itemsCount=" + (items != null ? items.size() : 0) +
                ", createdAt=" + createdAt +
                ", lastUpdatedAt=" + updatedAt +
                ", timeToLive=" + timeToLive +
                ", version=" + version +
                '}';
    }

    /**
     * Obtiene el identificador único del carrito.
     * @return el identificador único del carrito.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el identificador del usuario asociado al carrito.
     * @return el identificador del usuario, o {@code null} si no hay usuario asociado.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Obtiene la lista de elementos en el carrito.
     * @return una lista inmutable de objetos {@link CartItem} que representan los productos en el carrito.
     */
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Obtiene la fecha y hora en que se creó el carrito.
     * @return un objeto {@link LocalDateTime} con la fecha y hora de creación del carrito.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Obtiene la fecha y hora en que se actualizó por última vez el carrito.
     * @return un objeto {@link LocalDateTime} con la fecha y hora de la última actualización.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Obtiene el tiempo de vida (TTL) restante del carrito.
     * @return el tiempo de vida en días como un valor de tipo {@link Long}.
     */
    public Long getTimeToLive() {
        return timeToLive;
    }

    /**
     * Obtiene la versión actual del carrito.
     * Se utiliza para la gestión de concurrencia optimista.
     * @return la versión del carrito como un valor de tipo {@link Long}.
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Modifica el id del usuario
     * @param userId el identificador único del usuario. Puede ser nulo.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Modifica el tiempo de vida del objeto.
     * @param timeToLive el nuevo tiempo de vida en horas.
     */
    public void setTimeToLive(Long timeToLive) {
        if(timeToLive <= 0) {
            throw new IllegalArgumentException("timeToLive must be greater than zero");
        }
        this.timeToLive = timeToLive;
    }
}
