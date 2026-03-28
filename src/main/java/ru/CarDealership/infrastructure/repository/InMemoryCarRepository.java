package ru.CarDealership.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
  public Page<Car> findFiltered(CarFilter filter, Pageable pageable) {
    List<Car> filtered = storage.values().stream()
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
    
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), filtered.size());
    
    if (start > filtered.size()) {
      return new PageImpl<>(new ArrayList<>(), pageable, filtered.size());
    }
    
    List<Car> pageContent = filtered.subList(start, end);
    return new PageImpl<>(pageContent, pageable, filtered.size());
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

  @Override
  public Page<Car> findAll(Pageable pageable) {
    List<Car> allCars = new ArrayList<>(storage.values());
    int start = (int)  pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), allCars.size());

    if (start > allCars.size()) {
      return new PageImpl<>(new ArrayList<>(), pageable, allCars.size());
    }

    List<Car> pageContent = allCars.subList(start, end);
    return new PageImpl<>(pageContent, pageable, allCars.size());
  }
}
