package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.ApiResponse;
import ru.CarDealership.api.dto.CarFilterRequest;
import ru.CarDealership.api.dto.CarInfoResponse;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.api.mapper.CarDtoMapper;
import ru.CarDealership.service.CarService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@AllArgsConstructor
@Tag(name = "Cars", description = "Car management")
public class CarController {

    private final CarService carService;
    private final CarDtoMapper mapper;

    @GetMapping
    @Operation(summary = "Search cars with optional filters")
    public ApiResponse<Page<CarResponse>> search(CarFilterRequest filterRequest, Pageable pageable) {
        Page<CarResponse> cars = carService.searchCars(filterRequest, pageable).map(mapper::toResponse);
        return new ApiResponse<>(200, "Cars found", cars);
    }

    @GetMapping("/paged")
    @Operation(summary = "Get all cars paginated")
    public ApiResponse<Page<CarResponse>> getAllPaged(Pageable pageable) {
        return new ApiResponse<>(200, "OK", carService.findAllCarPaginated(pageable).map(mapper::toResponse));
    }

    @GetMapping("/test-drive")
    @Operation(summary = "Get cars available for test drive")
    public List<CarResponse> getTestDriveCars() {
        return carService.getTestDriveCars().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}/info")
    @Operation(summary = "Get car price info (internal use)")
    public CarInfoResponse getCarInfo(@PathVariable UUID id) {
        var car = carService.findCarById(id);
        return new CarInfoResponse(car.getId(), car.getPrice());
    }

    @PostMapping("/{id}/test-drive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add car to test drive fleet")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void addToTestDrive(@PathVariable UUID id) {
        carService.findCarById(id);
        carService.addCarToTestDrive(id);
    }

    @DeleteMapping("/{id}/test-drive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove car from test drive fleet")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void removeFromTestDrive(@PathVariable UUID id) {
        carService.findCarById(id);
        carService.removeCarFromTestDrive(id);
    }
}
