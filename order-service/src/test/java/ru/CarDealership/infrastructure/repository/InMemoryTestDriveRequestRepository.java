package ru.CarDealership.infrastructure.repository;

import ru.CarDealership.domain.order.TestDriveRequest;
import ru.CarDealership.domain.order.TestDriveRequestRepository;

import java.util.*;

public class InMemoryTestDriveRequestRepository implements TestDriveRequestRepository {
    private final Map<UUID, TestDriveRequest> storage = new HashMap<>();

    @Override
    public Optional<TestDriveRequest> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<TestDriveRequest> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public TestDriveRequest save(TestDriveRequest request) {
        storage.put(request.getId(), request);
        return request;
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}
