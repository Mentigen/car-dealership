package ru.CarDealership.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.CarDealership.api.dto.CarInfoResponse;
import ru.CarDealership.service.CarService;

import java.util.UUID;

@RestController
@RequestMapping("/api/car-configurations")
@AllArgsConstructor
@Tag(name = "Car Configurations", description = "Car configuration info")
public class CarConfigurationController {

    private final CarService carService;

    @GetMapping("/{id}/info")
    @Operation(summary = "Get car configuration price info (internal use)")
    public CarInfoResponse getConfigInfo(@PathVariable UUID id) {
        var config = carService.findConfigurationById(id);
        return new CarInfoResponse(config.getId(), config.getPrice());
    }
}
