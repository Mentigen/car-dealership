package ru.CarDealership.infrastructure.listener;

import jakarta.persistence.PrePersist;
import org.springframework.stereotype.Component;
import ru.CarDealership.infrastructure.entity.BaseEntity;

import java.util.UUID;

@Component
public class AuditListener {
    @PrePersist
    public void onPrePersist(BaseEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
    }
}
