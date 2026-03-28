package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter @Setter
@SQLRestriction("removed = false")
public abstract class BaseEntity {
    @Id
    private UUID id;

    private Instant createdAt;
    private Instant updatedAt;
    protected boolean removed = false;

    @PrePersist
    protected void prePersist() {
        if (id == null) this.id = UUID.randomUUID();
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = Instant.now();
    }
}
