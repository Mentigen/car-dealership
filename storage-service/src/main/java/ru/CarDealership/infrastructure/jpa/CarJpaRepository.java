package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.CarDealership.infrastructure.entity.CarEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarEntity, UUID>, JpaSpecificationExecutor<CarEntity> {
    Optional<CarEntity> findByCarConfiguration_Id(UUID configId);
    List<CarEntity> findByAvailableForTestDriveTrue();
}
