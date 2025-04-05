package com.ecommerce.catalog.sharedkernel.domain.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa una entidad base con propiedades y comportamientos comunes para objetos de dominio.
 * Esta clase está pensada para ser extendida por implementaciones de entidades concretas.
 * @param <ID> el tipo del identificador de la entidad, que debe extender Serializable.
 */
public abstract class BaseEntity<ID extends Serializable> implements Serializable, Persistable<ID> {
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

    /**
     * Constructor protegido con un identificador predeterminado.
     * @param id el identificador único de la entidad.
     */
    protected BaseEntity(ID id) {
        setId(id);
    }

    // --- Métodos heredados ---

    /**
     * Indica explícitamente a Spring Data si esta instancia es nueva.
     * Es nueva si la fecha de creación (gestionada por @CreatedDate) aún no ha sido asignada.
     * @return true si createdAt es null, false en caso contrario.
     */
    @Override
    public boolean isNew() {
        return this.createdAt == null;
    }

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

    @Override
    public ID getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // --- Setters ---

    public void setId(ID id) { this.id = Objects.requireNonNull(id, "id is required"); }
}
