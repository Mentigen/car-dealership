package ru.CarDealership.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CarResponse(
        UUID id,
        String brand,
        String modelName,
        BigDecimal price,
        String bodyType,
        String fuelType,
        String driveType,
        int enginePower,
        double engineVolume
) {}
