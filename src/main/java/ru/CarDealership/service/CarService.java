package ru.CarDealership.service;

import ru.CarDealership.domain.car.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.CarDealership.domain.car.*;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarService {
  private final CarModelRepository carModelRepository;
  private final PartRepository partRepository;
  private final CarRepository carRepository;

  public Car addCar(Car car) {
    return carRepository.save(car);
  }

  public Car updateCar(UUID id, CarConfiguration configuration) {
    Car car =
        carRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Car not found"));

    car.setCarConfiguration(configuration);

    return carRepository.save(car);
  }

  void deleteCar(UUID id) {
    carRepository.delete(id);
  }

  public List<Car> findAllCars() {
    return carRepository.findAll();
  }

  public Car findCarById(UUID id) {
    return carRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Car not found"));
  }

  public CarConfiguration findConfigurationById(UUID configId) {
    return carRepository.findAll().stream()
            .map(Car::getCarConfiguration)
            .filter(c -> c.getId().equals(configId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Car configuration not found"));
  }

  public List<CarModel> findAll() {
    return carModelRepository.findAll();
  }

  public List<Part> getAvailablePartsForModel(UUID modelId) {
    CarModel model =
        carModelRepository
            .findById(modelId)
            .orElseThrow(() -> new EntityNotFoundException("Car model not found"));
    return partRepository.findAll().stream().filter(part -> part.isCompatibleWith(model)).toList();
  }

  public List<Car> searchCars(CarFilter filter) {
    filter.validate();
    return carRepository.findAll().stream()
        .filter(CarSpecifications.hasBrand(filter.getBrand()))
        .filter(CarSpecifications.hasModel(filter.getModelName()))
        .filter(CarSpecifications.hasBodyType(filter.getBodyType()))
        .filter(CarSpecifications.hasFuelType(filter.getFuelType()))
        .filter(CarSpecifications.hasDriveType(filter.getDriveType()))
        .filter(CarSpecifications.hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice()))
        .filter(CarSpecifications.hasPowerBetween(filter.getMinPower(), filter.getMaxPower()))
        .filter(
            CarSpecifications.hasEngineVolumeBetween(
                filter.getMinEngineVolume(), filter.getMaxEngineVolume()))
        .filter(CarSpecifications.hasColor(filter.getColor()))
        .filter(CarSpecifications.hasTransmissionType(filter.getTransmissionType()))
        .toList();
  }
}
