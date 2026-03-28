package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.CarDealership.infrastructure.entity.CarModelEntity;

import java.util.UUID;

public interface CarModelJpaRepository extends JpaRepository<CarModelEntity, UUID>, JpaSpecificationExecutor<CarModelEntity> {
}
