package org.example.infrastructure.repository;

import org.example.domain.order.TestDriveRequest;
import org.example.domain.order.TestDriveRequestRepository;

import java.util.*;

public class InMemoryTestDriveRequestRepository implements TestDriveRequestRepository {
  private final HashMap<UUID, TestDriveRequest> storage = new HashMap<UUID, TestDriveRequest>();

  @Override
  public List<TestDriveRequest> findByStatus(String status) {
    return storage.values().stream()
        .filter(request -> request.getStatus().name().equalsIgnoreCase(status))
        .toList();
  }

  @Override
  public List<TestDriveRequest> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public Optional<TestDriveRequest> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public TestDriveRequest save(TestDriveRequest testDriveRequest) {
    storage.put(testDriveRequest.getId(), testDriveRequest);
    return testDriveRequest;
  }

  @Override
  public void delete(UUID id) {
    storage.remove(id);
  }
}
