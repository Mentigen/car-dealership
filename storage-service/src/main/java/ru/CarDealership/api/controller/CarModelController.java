package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.CarModelRequest;
import ru.CarDealership.api.dto.CarModelResponse;
import ru.CarDealership.api.mapper.CarModelDtoMapper;
import ru.CarDealership.domain.car.*;
import ru.CarDealership.service.CarModelService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/car-models")
@AllArgsConstructor
@Tag(name = "Car Models", description = "Car model management")
public class CarModelController {

    private final CarModelService carModelService;
    private final CarModelDtoMapper mapper;

    @GetMapping
    @Operation(summary = "Get all car models")
    public List<CarModelResponse> findAll() {
        return carModelService.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car model by id")
    public CarModelResponse findById(@PathVariable UUID id) {
        return mapper.toResponse(carModelService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create car model")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public CarModelResponse create(@Valid @RequestBody CarModelRequest request) {
        CarModel model = new CarModel(null, request.getBrand(), request.getModelName(),
                new Price(request.getPrice()), new EnginePower(request.getEnginePower()),
                new EngineVolume(request.getEngineVolume()), request.getFuelType(), request.getBody(), request.getDrive());
        return mapper.toResponse(carModelService.save(model));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete car model")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void delete(@PathVariable UUID id) {
        carModelService.delete(id);
    }
}
