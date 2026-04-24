package ru.CarDealership.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.CarDealership.api.dto.CarFilterRequest;
import ru.CarDealership.domain.car.*;
import ru.CarDealership.infrastructure.repository.InMemoryCarModelRepository;
import ru.CarDealership.infrastructure.repository.InMemoryCarRepository;
import ru.CarDealership.infrastructure.repository.InMemoryPartRepository;
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
        modelBMW.setEnginePower(new EnginePower(184));
        modelBMW.setEngineVolume(new EngineVolume(2.0));
        modelBMW.setFuel(FuelType.PETROL);
        modelBMW.setBody(BodyType.SEDAN);
        modelBMW.setDrive(DriveType.RWD);
        modelBMW.setPrice(new Price(new BigDecimal("3000000")));
        carModelRepository.save(modelBMW);

        List<UUID> compatibleIds = List.of(modelBMW.getId());

        wheelPart = new Part(UUID.randomUUID(), PartType.WHEEL, new BigDecimal("50000"), compatibleIds);
        transmissionPart = new TransmissionPart(UUID.randomUUID(), PartType.TRANSMISSION,
                new BigDecimal("100000"), compatibleIds, TransmissionType.AUTOMATIC);
        steeringWheelPart = new Part(UUID.randomUUID(), PartType.STEERING_WHEEL, new BigDecimal("20000"), compatibleIds);
        interiorPart = new Part(UUID.randomUUID(), PartType.INTERIOR, new BigDecimal("150000"), compatibleIds);
        colorPart = new ColorPart(UUID.randomUUID(), PartType.COLOR, new BigDecimal("0"), compatibleIds, "Black");

        partRepository.save(wheelPart);
        partRepository.save(transmissionPart);
        partRepository.save(steeringWheelPart);
        partRepository.save(interiorPart);
        partRepository.save(colorPart);
    }

    private Car buildCar() {
        CarConfiguration config = new CarConfiguration.Builder(modelBMW)
                .addPart(wheelPart)
                .addPart(transmissionPart)
                .addPart(steeringWheelPart)
                .addPart(interiorPart)
                .addPart(colorPart)
                .build();
        return new Car(UUID.randomUUID(), config);
    }

    @Test
    void testSearchCarsByBrandAndModel() {
        carService.addCar(buildCar());
        CarFilterRequest filter = new CarFilterRequest();
        filter.setBrand("BMW");
        filter.setModelName("320i");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Car> result = carService.searchCars(filter, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("BMW", result.getContent().get(0).getCarConfiguration().getModel().getBrand());
    }

    @Test
    void testSearchCarsByColor() {
        carService.addCar(buildCar());
        Pageable pageable = PageRequest.of(0, 10);

        CarFilterRequest blackFilter = new CarFilterRequest();
        blackFilter.setColor("Black");
        CarFilterRequest whiteFilter = new CarFilterRequest();
        whiteFilter.setColor("White");

        assertEquals(1, carService.searchCars(blackFilter, pageable).getTotalElements());
        assertEquals(0, carService.searchCars(whiteFilter, pageable).getTotalElements());
    }

    @Test
    void testSearchCarsByTransmission() {
        carService.addCar(buildCar());
        Pageable pageable = PageRequest.of(0, 10);

        CarFilterRequest autoFilter = new CarFilterRequest();
        autoFilter.setTransmissionType(TransmissionType.AUTOMATIC);
        CarFilterRequest manualFilter = new CarFilterRequest();
        manualFilter.setTransmissionType(TransmissionType.MANUAL);

        assertEquals(1, carService.searchCars(autoFilter, pageable).getTotalElements());
        assertEquals(0, carService.searchCars(manualFilter, pageable).getTotalElements());
    }

    @Test
    void testUpdateCar() {
        Car car = buildCar();
        carService.addCar(car);
        CarConfiguration newConfig = new CarConfiguration.Builder(modelBMW)
                .addPart(wheelPart)
                .addPart(transmissionPart)
                .addPart(steeringWheelPart)
                .addPart(interiorPart)
                .addPart(colorPart)
                .build();
        Car updated = carService.updateCar(car.getId(), newConfig);
        assertEquals(newConfig, updated.getCarConfiguration());
    }

    @Test
    void testDeleteCar() {
        Car car = buildCar();
        carService.addCar(car);
        carService.deleteCar(car.getId());

        CarFilterRequest filter = new CarFilterRequest();
        filter.setBrand("BMW");
        assertTrue(carService.searchCars(filter, PageRequest.of(0, 10)).isEmpty());
    }

    @Test
    void testGetAvailablePartsForModel() {
        List<Part> parts = carService.getAvailablePartsForModel(modelBMW.getId());
        assertEquals(5, parts.size());
    }

    @Test
    void testFindAllModels() {
        List<CarModel> models = carModelRepository.findAll();
        assertEquals(1, models.size());
        assertEquals(modelBMW, models.get(0));
    }

    @Test
    void testManageTestDriveCars() {
        Car car = buildCar();
        carService.addCar(car);

        carService.addCarToTestDrive(car.getId());
        List<Car> available = carService.getTestDriveCars();
        assertEquals(1, available.size());
        assertEquals(car.getId(), available.get(0).getId());

        carService.removeCarFromTestDrive(car.getId());
        assertTrue(carService.getTestDriveCars().isEmpty());
    }
}
