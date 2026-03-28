package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.ApiResponse;
import ru.CarDealership.api.dto.CarFilterRequest;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.api.mapper.CarDtoMapper;
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
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cars found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid filter parameters"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ApiResponse<Page<CarResponse>> search(CarFilterRequest filterRequest, Pageable pageable) {
        Page<CarResponse> cars = carService.searchCars(filterRequest, pageable)
                .map(mapper::toResponse);
        return new ApiResponse<>(200, "Cars found", cars);
    }

    @GetMapping("/paged")
    @Operation(summary = "Get all cars paginated")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Page of cars"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ApiResponse<Page<CarResponse>> getAllPaged(Pageable pageable) {
        return new ApiResponse<>(200, "OK",
                carService.findAllCarPaginated(pageable).map(mapper::toResponse));
    }

    @GetMapping("/test-drive")
    @Operation(summary = "Get cars available for test drive")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Test drive cars retrieved"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<CarResponse> getTestDriveCars() {
        return testDriveService.getTestDriveCars().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping("/{id}/test-drive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add car to test drive fleet")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Car added to test drive"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid car ID format"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Car not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void addToTestDrive(@PathVariable UUID id) {
        testDriveService.addCarToTestDrive(carService.findCarById(id));
    }

    @DeleteMapping("/{id}/test-drive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove car from test drive fleet")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Car removed from test drive"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid car ID format"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Car not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void removeFromTestDrive(@PathVariable UUID id) {
        testDriveService.removeCarFromTestDrive(carService.findCarById(id));
    }
}
