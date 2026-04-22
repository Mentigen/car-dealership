package ru.CarDealership.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.CarDealership.infrastructure.entity.TestDriveRequestEntity;

import java.util.UUID;

public interface TestDriveRequestJpaRepository extends JpaRepository<TestDriveRequestEntity, UUID> {
}
