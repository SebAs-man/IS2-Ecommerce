package com.ecommerce.catalog.product.Domain.model;

import com.ecommerce.catalog.product.Domain.constant.AttributeKey;
import com.ecommerce.catalog.sharedkernel.domain.model.BaseEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Clase entidad que representa un objeto producto en la base de datos.
 */
@Document(collection = "products")
public class Product extends BaseEntity<ObjectId>{
    //Campos básicos.
    private String name;
    private Double price;
    private Integer quantity;
    private Boolean available;
    private String description;

    //Lista de referencias a otros documentos.
    //private List<Integer> sellers;
    //private List<Integer> reviews;
    //private List<Integer> questions;
    //private List<Integer> categories;

    //Lista incrustada con atributos dinámicos
    private final List<Attribute> attributes;

    /**
     * Constructor sin argumentos.
     */
    public Product() {
        super();
        this.attributes = Collections.emptyList();
    }

    /**
     * Constructor de un producto con todos los argumentos.
     * @param name nombre del producto.
     * @param price precio del producto.
     * @param quantity cantidad en el inventario del producto.
     * @param available disponibilidad en el catálogo del producto.
     * @param description descripción del producto.
     * @param attributes atributos adicionales para dar más información del producto.
     */
    public Product(String name, Double price, Integer quantity,
                   Boolean available, String description, List<Attribute> attributes) {
        this.name = Objects.requireNonNull(name, " name is required");
        this.price = Objects.requireNonNull(price, "price is required");
        if(price < 0){
            throw new IllegalArgumentException("price cannot be negative");
        }
        this.quantity = Objects.requireNonNull(quantity, "quantity is required");
        this.available = Objects.requireNonNull(available, "available is required");
        this.description = description;
        this.attributes = Objects.requireNonNull(attributes, "attributes is required");
    }

    /**
     * Obtener el precio del producto
     * @return un decimal double
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Cambiar el precio del producto.
     * @param price nuevo precio.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Obtener el nombre del producto.
     * @return una cadena de caracteres
     */
    public String getName() {
        return name;
    }

    /**
     * Cambiar el nombre del producto.
     * @param name nuevo nombre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtener la cantidad en inventario
     * @return un número entero mayor a -1
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Cambiar la cantidad en inventario
     * @param quantity nueva cantidad
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Obtener la disponibilidad del producto
     * @return true si está disponible, false de lo contrario.
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * Cambiar el estado de disponibilidad el producto
     * @param available nuevo estado
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * Obtener la descripción del producto.
     * @return una cadena de caracteres.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Cambiar la descripción del producto.
     * @param description la nueva descripción.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtener todos los atributos adicionales del producto.
     * @return listado de atributos.
     */
    public List<Attribute> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    /**
     * Añade un nuevo atributo o actualiza uno existente con la misma clave.
     * @param attribute una instancia ya validada del atributo a añadir o actualizar.
     */
    public void addOrUpdateAttribute(Attribute attribute) {
        Objects.requireNonNull(attribute, "attribute is required");
        //Verifica si ya existe el atributo para modificarlo
        this.attributes.removeIf(attr -> attr.getKey() == attribute.getKey());
        this.attributes.add(attribute);
    }

    /**
     * Elimina un atributo basado en su clave.
     * @param key La clave del atributo a eliminar.
     */
    public void removeAttribute(String key) {
        if(key != null && !key.isBlank()){
            AttributeKey keyObj = null;
            try{ //Convierte el String de entrada en un valor de la enumeración
                keyObj = AttributeKey.valueOf(key.toUpperCase(Locale.ROOT).trim());
            } catch (IllegalArgumentException e){
                return;
            }
            final AttributeKey finalKeyToRemove = keyObj;
            this.attributes.removeIf(attr -> attr.getKey() == finalKeyToRemove);
        }
    }
}
