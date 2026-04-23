package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.CarDealership.infrastructure.entity.AssemblyOrderEntity;

import java.util.UUID;

public interface AssemblyOrderJpaRepository extends JpaRepository<AssemblyOrderEntity, UUID> {
    boolean existsBySourceOrderId(UUID sourceOrderId);
}
