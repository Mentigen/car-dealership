package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.CarDealership.infrastructure.entity.CarEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarEntity, UUID>, JpaSpecificationExecutor<CarEntity> {
    @EntityGraph(attributePaths = {"carConfiguration", "carConfiguration.carModel", "carConfiguration.parts"})
    List<CarEntity> findByAvailableForTestDriveTrue();

    @EntityGraph(attributePaths = {"carConfiguration", "carConfiguration.carModel", "carConfiguration.parts"})
    Optional<CarEntity> findByCarConfiguration_Id(UUID configurationId);

    @EntityGraph(attributePaths = {"carConfiguration", "carConfiguration.carModel", "carConfiguration.parts"})
    List<CarEntity> findAll();

    @EntityGraph(attributePaths = {"carConfiguration", "carConfiguration.carModel", "carConfiguration.parts"})
    Page<CarEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"carConfiguration", "carConfiguration.carModel", "carConfiguration.parts"})
    Optional<CarEntity> findById(UUID id);

    @EntityGraph(attributePaths = {"carConfiguration", "carConfiguration.carModel", "carConfiguration.parts"})
    List<CarEntity> findAll(org.springframework.data.jpa.domain.Specification<CarEntity> spec);

    @EntityGraph(attributePaths = {"carConfiguration", "carConfiguration.carModel", "carConfiguration.parts"})
    Page<CarEntity> findAll(org.springframework.data.jpa.domain.Specification<CarEntity> spec, Pageable pageable);
}