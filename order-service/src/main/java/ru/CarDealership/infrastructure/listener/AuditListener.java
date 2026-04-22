package ru.CarDealership.infrastructure.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.CarDealership.infrastructure.entity.BaseEntity;

import java.time.Instant;

public class AuditListener {
    @PrePersist
    public void prePersist(BaseEntity entity) {
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
    }
}
