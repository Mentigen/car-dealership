package ru.CarDealership.domain.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
  public Optional<Car> findById(UUID id);

  public Optional<CarConfiguration> findByConfigurationId(UUID configId);

  public List<Car> findAll();

  public List<Car> findFiltered(CarFilter filter);

  public Car save(Car car);

  public void delete(UUID id);

  public void addTestDriveCar(UUID carId);

  public void removeTestDriveCar(UUID carId);

  public List<Car> findTestDriveCars();
}
