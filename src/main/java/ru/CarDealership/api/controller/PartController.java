package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.PartResponse;
import ru.CarDealership.api.mapper.PartDtoMapper;
import ru.CarDealership.service.CarService;
import ru.CarDealership.service.PartService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parts")
@AllArgsConstructor
@Tag(name = "Parts", description = "Car parts management")
public class PartController {

    private final PartService partService;
    private final CarService carService;
    private final PartDtoMapper mapper;

    @GetMapping
    @Operation(summary = "Get all parts")
    public List<PartResponse> findAll() {
        return partService.getAllParts().stream()
                .map(mapper::toPolymorphicResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get part by id")
    public PartResponse findById(@PathVariable UUID id) {
        return partService.getPartById(id)
                .map(mapper::toPolymorphicResponse)
                .orElseThrow(() -> new ru.CarDealership.domain.exceptions.EntityNotFoundException("Part not found"));
    }

    @GetMapping("/compatible/{modelId}")
    @Operation(summary = "Get parts compatible with car model")
    public List<PartResponse> findCompatibleWithModel(@PathVariable UUID modelId) {
        return carService.getAvailablePartsForModel(modelId).stream()
                .map(mapper::toPolymorphicResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete part")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void delete(@PathVariable UUID id) {
        partService.deletePart(id);
    }
}
