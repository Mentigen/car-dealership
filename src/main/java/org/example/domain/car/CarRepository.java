package org.example.domain.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
  public Optional<Car> findById(UUID id);

  public List<Car> findAll();

  public Car save(Car car);

  public void delete(UUID id);

  public void addTestDriveCar(UUID carId);

  public void removeTestDriveCar(UUID carId);

  public List<Car> findTestDriveCars();
}
