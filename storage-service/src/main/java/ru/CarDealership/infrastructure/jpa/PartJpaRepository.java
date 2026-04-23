package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.CarDealership.infrastructure.entity.PartEntity;

import java.util.UUID;

public interface PartJpaRepository extends JpaRepository<PartEntity, UUID> {
}
