package CarDealership.domain.order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestDriveRequestRepository {
  public List<TestDriveRequest> findByStatus(String status);

  public List<TestDriveRequest> findAll();

  public Optional<TestDriveRequest> findById(UUID id);

  public TestDriveRequest save(TestDriveRequest testDriveRequest);

  public void delete(UUID id);
}
