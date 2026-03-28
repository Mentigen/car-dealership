package ru.CarDealership.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.api.dto.CarFilterRequest;
import ru.CarDealership.domain.car.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
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

  @Transactional(readOnly = true)
  public List<Car> findAllCars() {
    return carRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Car findCarById(UUID id) {
    return carRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Car not found"));
  }

  @Transactional(readOnly = true)
  public CarConfiguration findConfigurationById(UUID configId) {
    return carRepository.findByConfigurationId(configId)
            .orElseThrow(() -> new EntityNotFoundException("Car configuration not found"));
  }

  @Transactional(readOnly = true)
  public List<CarModel> findAll() {
    return carModelRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Part> getAvailablePartsForModel(UUID modelId) {
    CarModel model =
        carModelRepository
            .findById(modelId)
            .orElseThrow(() -> new EntityNotFoundException("Car model not found"));
    return partRepository.findAll().stream().filter(part -> part.isCompatibleWith(model)).toList();
  }

  @Transactional(readOnly = true)
  public Page<Car> searchCars(CarFilterRequest filterRequest, Pageable pageable) {
    CarFilter filter = CarFilter.builder()
            .brand(filterRequest.getBrand())
            .modelName(filterRequest.getModelName())
            .bodyType(filterRequest.getBodyType())
            .fuelType(filterRequest.getFuelType())
            .minPower(filterRequest.getMinPower())
            .maxPower(filterRequest.getMaxPower())
            .minEngineVolume(filterRequest.getMinEngineVolume())
            .maxEngineVolume(filterRequest.getMaxEngineVolume())
            .transmissionType(filterRequest.getTransmissionType())
            .color(filterRequest.getColor())
            .driveType(filterRequest.getDriveType())
            .minPrice(filterRequest.getMinPrice())
            .maxPrice(filterRequest.getMaxPrice())
            .build();
    filter.validate();
    return carRepository.findFiltered(filter, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Car> findAllCarPaginated(Pageable pageable) {
    return carRepository.findAll(pageable);
  }
}
