package org.example.infrastructure.repository;

import org.example.domain.car.CarModel;
import org.example.domain.car.CarModelRepository;

import java.util.*;

public class InMemoryCarModelRepository implements CarModelRepository {
  private final HashMap<UUID, CarModel> storage = new HashMap<>();

  @Override
  public Optional<CarModel> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public List<CarModel> findAll() {
    return new ArrayList<CarModel>(storage.values());
  }

  @Override
  public CarModel save(CarModel model) {
    if (model.getId() == null) {
      model.setId(UUID.randomUUID());
    }
    storage.put(model.getId(), model);

    return model;
  }

  @Override
  public void delete(UUID id) {
    storage.remove(id);
  }
}
