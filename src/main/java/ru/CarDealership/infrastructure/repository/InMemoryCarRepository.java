package ru.CarDealership.infrastructure.repository;

import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.car.CarConfiguration;
import ru.CarDealership.domain.car.CarFilter;
import ru.CarDealership.domain.car.CarRepository;
import ru.CarDealership.service.CarSpecifications;

import java.util.*;

public class InMemoryCarRepository implements CarRepository {
  private final HashMap<UUID, Car> storage = new HashMap<>();
  private final Set<UUID> testDriveCars = new HashSet<>();

  @Override
  public Car save(Car car) {
    if (car.getId() == null) {
      car.setId(UUID.randomUUID());
    }
    storage.put(car.getId(), car);
    return car;
  }

  @Override
  public Optional<Car> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Optional<CarConfiguration> findByConfigurationId(UUID configId) {
    return storage.values().stream()
        .filter(car -> car.getCarConfiguration().getId().equals(configId))
        .findFirst()
        .map(Car::getCarConfiguration);
  }

  @Override
  public List<Car> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public List<Car> findFiltered(CarFilter filter) {
    return storage.values().stream()
        .filter(CarSpecifications.hasBrand(filter.getBrand()))
        .filter(CarSpecifications.hasModel(filter.getModelName()))
        .filter(CarSpecifications.hasBodyType(filter.getBodyType()))
        .filter(CarSpecifications.hasFuelType(filter.getFuelType()))
        .filter(CarSpecifications.hasDriveType(filter.getDriveType()))
        .filter(CarSpecifications.hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice()))
        .filter(CarSpecifications.hasPowerBetween(filter.getMinPower(), filter.getMaxPower()))
        .filter(CarSpecifications.hasEngineVolumeBetween(filter.getMinEngineVolume(), filter.getMaxEngineVolume()))
        .filter(CarSpecifications.hasColor(filter.getColor()))
        .filter(CarSpecifications.hasTransmissionType(filter.getTransmissionType()))
        .toList();
  }

  @Override
  public void delete(UUID id) {
    storage.remove(id);
    testDriveCars.remove(id);
  }

  @Override
  public void addTestDriveCar(UUID carId) {
    if (storage.containsKey(carId)) {
      testDriveCars.add(carId);
    }
  }

  @Override
  public void removeTestDriveCar(UUID carId) {
    testDriveCars.remove(carId);
  }

  @Override
  public List<Car> findTestDriveCars() {
    return testDriveCars.stream().map(storage::get).filter(Objects::nonNull).toList();
  }
}
