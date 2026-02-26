package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.car.*;
import org.example.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

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
    return carRepository.findAll().stream()
        .filter(
            car -> {
              CarModel model = car.getCarConfiguration().getModel();

              if (filter.getMinPrice() != null
                  && car.getPrice().compareTo(filter.getMinPrice()) < 0) return false;
              if (filter.getMaxPrice() != null
                  && car.getPrice().compareTo(filter.getMaxPrice()) > 0) return false;

              if (filter.getBrand() != null
                  && !model.getBrand().equalsIgnoreCase(filter.getBrand())) return false;
              if (filter.getModelName() != null
                  && !model.getModelName().equalsIgnoreCase(filter.getModelName())) return false;

              if (filter.getBodyType() != null && model.getBody() != filter.getBodyType())
                return false;
              if (filter.getFuelType() != null && model.getFuel() != filter.getFuelType())
                return false;
              if (filter.getDriveType() != null && model.getDrive() != filter.getDriveType())
                return false;

              if (filter.getMinPower() != null && model.getEnginePower() < filter.getMinPower())
                return false;
              if (filter.getMaxPower() != null && model.getEnginePower() > filter.getMaxPower())
                return false;
              if (filter.getMinEngineVolume() != null
                  && model.getEngineVolume() < filter.getMinEngineVolume()) return false;
              if (filter.getMaxEngineVolume() != null
                  && model.getEngineVolume() > filter.getMaxEngineVolume()) return false;

              if (filter.getColor() != null) {
                boolean hasColor =
                    car.getCarConfiguration().getParts().stream()
                        .filter(part -> part instanceof ColorPart)
                        .map(part -> (ColorPart) part)
                        .anyMatch(
                            colorPart -> colorPart.getColor().equalsIgnoreCase(filter.getColor()));

                if (!hasColor) return false;
              }

              if (filter.getTransmissionType() != null) {
                boolean hasTransmission =
                    car.getCarConfiguration().getParts().stream()
                        .filter(part -> part instanceof TransmissionPart)
                        .map(part -> (TransmissionPart) part)
                        .anyMatch(tp -> tp.getTransmissionType() == filter.getTransmissionType());

                if (!hasTransmission) return false;
              }

              return true;
            })
        .toList();
  }
}
