package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@SQLRestriction("removed = false")
public abstract class BaseEntity {
    @Id
    protected UUID id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    protected boolean removed = false;

    @PrePersist
    protected void generateId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public void setId(UUID id) { this.id = id; }
}
