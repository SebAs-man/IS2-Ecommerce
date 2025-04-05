package com.ecommerce.catalog.sharedkernel.domain.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase base abstracta para entidades persistentes dentro del microservicio de Catálogo.
 * Proporciona un ID y campos de auditoría básicos (fechas de creación/modificación).
 * @param <ID> El tipo del identificador, debe ser Serializable.
 */
public abstract class BaseEntity<ID extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Para versionado de serialización

    @Id
    private ID id;

    @CreatedDate // Gestionado por Spring Data Auditing
    private LocalDateTime createdAt;

    @LastModifiedDate // Gestionado por Spring Data Auditing
    private LocalDateTime updatedAt;

    /**
     * Constructor protegido sin argumentos para frameworks.
     */
    protected BaseEntity() {}

    // --- Métodos funcionales ---

    protected void touch(){
        this.updatedAt = LocalDateTime.now();
    }

    // --- Métodos heredados ---

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        BaseEntity<?> other = (BaseEntity<?>) obj;
        return this.id != null && Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "entity=" + getClass().getSimpleName() +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // --- Getters ---

    public ID getId() {
        return id;
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // --- Setters ---

    public void setId(ID id) {
        this.id = Objects.requireNonNull(id, "id is required");
    }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required"); }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required"); }
}
