package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.CarDealership.infrastructure.entity.CarModelEntity;

import java.util.UUID;

public interface CarModelJpaRepository extends JpaRepository<CarModelEntity, UUID> {
}
