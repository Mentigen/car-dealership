package ru.CarDealership.domain.order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestDriveRequestRepository {
    Optional<TestDriveRequest> findById(UUID id);
    List<TestDriveRequest> findAll();
    TestDriveRequest save(TestDriveRequest request);
    void delete(UUID id);
}
