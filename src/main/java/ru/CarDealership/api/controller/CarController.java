package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.CarFilterRequest;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.api.mapper.CarDtoMapper;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.car.CarFilter;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.service.CarService;
import ru.CarDealership.service.TestDriveService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@AllArgsConstructor
@Tag(name = "Cars", description = "Car management")
public class CarController {

    private final CarService carService;
    private final TestDriveService testDriveService;
    private final CarDtoMapper mapper;

    @GetMapping
    @Operation(summary = "Search cars with optional filters")
    public List<CarResponse> search(CarFilterRequest filterRequest) {
        CarFilter filter = CarFilter.builder()
                .brand(filterRequest.getBrand())
                .modelName(filterRequest.getModelName())
                .bodyType(filterRequest.getBodyType())
                .fuelType(filterRequest.getFuelType())
                .minPower(filterRequest.getMinPower())
                .maxPower(filterRequest.getMaxPower())
                .minEngineVolume(filterRequest.getMinEngineVolume())
                .maxEngineVolume(filterRequest.getMaxEngineVolume())
                .minPrice(filterRequest.getMinPrice())
                .maxPrice(filterRequest.getMaxPrice())
                .transmissionType(filterRequest.getTransmissionType())
                .color(filterRequest.getColor())
                .driveType(filterRequest.getDriveType())
                .build();
        return carService.searchCars(filter).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/test-drive")
    @Operation(summary = "Get cars available for test drive")
    public List<CarResponse> getTestDriveCars() {
        return testDriveService.getTestDriveCars().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping("/{id}/test-drive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add car to test drive fleet")
    public void addToTestDrive(@PathVariable UUID id) {
        testDriveService.addCarToTestDrive(carService.findCarById(id));
    }

    @DeleteMapping("/{id}/test-drive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove car from test drive fleet")
    public void removeFromTestDrive(@PathVariable UUID id) {
        testDriveService.removeCarFromTestDrive(carService.findCarById(id));
    }
}
