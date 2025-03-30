package com.ecommerce.catalog.sharedkernel.domain.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Clase entidad genérica.
 * Todas las entidades deberían heredar de esta clase.
 */
public abstract class BaseEntity<ID extends Serializable> {
    @Id
    @Indexed(unique = true)
    private ID id;

    @CreatedDate
    private Date created;// fecha de creación, inmutable.

    @LastModifiedDate
    private Date updated;// fecha de la última modificación.

    /**
     * Constructor sin argumentos.
     * Se define su fecha de creación.
     */
    public BaseEntity() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(id == null || obj == null || getClass() != obj.getClass()) return false;
        BaseEntity<?> other = (BaseEntity<?>) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
