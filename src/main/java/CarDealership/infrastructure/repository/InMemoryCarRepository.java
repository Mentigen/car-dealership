package CarDealership.infrastructure.repository;

import CarDealership.domain.car.Car;
import CarDealership.domain.car.CarRepository;

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
  public List<Car> findAll() {
    return new ArrayList<>(storage.values());
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
