package org.example.service;

import org.example.domain.car.*;
import org.example.infrastructure.repository.InMemoryCarModelRepository;
import org.example.infrastructure.repository.InMemoryCarRepository;
import org.example.infrastructure.repository.InMemoryPartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {

  private CarService carService;
  private InMemoryCarRepository carRepository;
  private InMemoryPartRepository partRepository;
  private InMemoryCarModelRepository carModelRepository;

  private CarModel modelBMW;
  private Part wheelPart;
  private Part transmissionPart;
  private Part steeringWheelPart;
  private Part interiorPart;
  private Part colorPart;

  @BeforeEach
  void setUp() {
    carRepository = new InMemoryCarRepository();
    partRepository = new InMemoryPartRepository();
    carModelRepository = new InMemoryCarModelRepository();
    carService = new CarService(carModelRepository, partRepository, carRepository);

    setupTestData();
  }

  private void setupTestData() {
    modelBMW = new CarModel();
    modelBMW.setId(UUID.randomUUID());
    modelBMW.setBrand("BMW");
    modelBMW.setModelName("320i");
    modelBMW.setEnginePower(184);
    modelBMW.setEngineVolume(2.0);
    modelBMW.setFuel(FuelType.PETROL);
    modelBMW.setBody(BodyType.SEDAN);
    modelBMW.setDrive(DriveType.RWD);
    modelBMW.setPrice(new BigDecimal("3000000"));
    carModelRepository.save(modelBMW);

    List<UUID> compatibleIds = List.of(modelBMW.getId());

    wheelPart = new Part(UUID.randomUUID(), PartType.WHEEL, new BigDecimal("50000"), compatibleIds);
    transmissionPart =
        new TransmissionPart(
            UUID.randomUUID(),
            PartType.TRANSMISSION,
            new BigDecimal("100000"),
            compatibleIds,
            TransmissionType.AUTOMATIC);
    steeringWheelPart =
        new Part(
            UUID.randomUUID(), PartType.STEERING_WHEEL, new BigDecimal("20000"), compatibleIds);
    interiorPart =
        new Part(UUID.randomUUID(), PartType.INTERIOR, new BigDecimal("150000"), compatibleIds);
    colorPart =
        new ColorPart(
            UUID.randomUUID(), PartType.COLOR, new BigDecimal("0"), compatibleIds, "Black");

    partRepository.save(wheelPart);
    partRepository.save(transmissionPart);
    partRepository.save(steeringWheelPart);
    partRepository.save(interiorPart);
    partRepository.save(colorPart);
  }

  @Test
  void testSearchCarsByBrandAndModel() {
    CarConfiguration config =
        new CarConfiguration.Builder(modelBMW)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    Car car = new Car(UUID.randomUUID(), config);
    carService.addCar(car);

    CarFilter filter = CarFilter.builder().brand("BMW").modelName("320i").build();

    List<Car> foundCars = carService.searchCars(filter);

    assertEquals(1, foundCars.size());
    assertEquals("BMW", foundCars.get(0).getCarConfiguration().getModel().getBrand());
  }

  @Test
  void testSearchCarsByColor() {
    CarConfiguration config =
        new CarConfiguration.Builder(modelBMW)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    Car car = new Car(UUID.randomUUID(), config);
    carService.addCar(car);

    CarFilter firstFilter = CarFilter.builder().color("Black").build();

    CarFilter secondFilter = CarFilter.builder().color("White").build();

    List<Car> firstFoundCars = carService.searchCars(firstFilter);
    List<Car> secondFoundCars = carService.searchCars(secondFilter);

    assertEquals(1, firstFoundCars.size());
    assertEquals(0, secondFoundCars.size());
  }

  @Test
  void testSearchCarsByTransmission() {
    CarConfiguration config =
        new CarConfiguration.Builder(modelBMW)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();

    Car car = new Car(UUID.randomUUID(), config);
    carService.addCar(car);

    CarFilter firstFilter =
        CarFilter.builder().transmissionType(TransmissionType.AUTOMATIC).build();

    CarFilter secondFilter = CarFilter.builder().transmissionType(TransmissionType.MANUAL).build();

    List<Car> firstFoundCars = carService.searchCars(firstFilter);
    List<Car> secondFoundCars = carService.searchCars(secondFilter);

    assertEquals(1, firstFoundCars.size());
    assertEquals(0, secondFoundCars.size());
  }

  @Test
  void testUpdateCar() {
    CarConfiguration config =
        new CarConfiguration.Builder(modelBMW)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();
    Car car = new Car(UUID.randomUUID(), config);
    carService.addCar(car);

    Car updated = carService.updateCar(car.getId(), config);
    assertEquals(config, updated.getCarConfiguration());
  }

  @Test
  void testDeleteCar() {
    CarConfiguration config =
        new CarConfiguration.Builder(modelBMW)
            .addPart(wheelPart)
            .addPart(transmissionPart)
            .addPart(steeringWheelPart)
            .addPart(interiorPart)
            .addPart(colorPart)
            .build();
    Car car = new Car(UUID.randomUUID(), config);
    carService.addCar(car);

    carService.deleteCar(car.getId());
    CarFilter filter = CarFilter.builder().brand("BMW").build();
    assertTrue(carService.searchCars(filter).isEmpty());
  }

  @Test
  void testGetAvailablePartsForModel() {
    List<Part> parts = carService.getAvailablePartsForModel(modelBMW.getId());
    assertEquals(5, parts.size());
  }

  @Test
  void testFindAllModels() {
    List<CarModel> models = carService.findAll();
    assertEquals(1, models.size());
    assertEquals(modelBMW, models.get(0));
  }
}
