package ru.CarDealership.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.CarDealership.api.dto.CarResponse;
import ru.CarDealership.grpc.GrpcCarClient;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarCatalogController {

    private final GrpcCarClient grpcCarClient;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<CarResponse> getAvailableCars() {
        return grpcCarClient.getAvailableCars();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public CarResponse getCarById(@PathVariable UUID id) {
        return grpcCarClient.getCarById(id);
    }
}
