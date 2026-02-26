package org.example.domain.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarModelRepository {
  public Optional<CarModel> findById(UUID id);

  public List<CarModel> findAll();

  public CarModel save(CarModel model);

  public void delete(UUID id);
}
