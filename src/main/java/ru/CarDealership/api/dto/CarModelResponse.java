package ru.CarDealership.api.dto;

import ru.CarDealership.domain.car.BodyType;
import ru.CarDealership.domain.car.DriveType;
import ru.CarDealership.domain.car.FuelType;

import java.math.BigDecimal;
import java.util.UUID;

public record CarModelResponse(
        UUID id,
        String brand,
        String modelName,
        BigDecimal price,
        int enginePower,
        double engineVolume,
        FuelType fuelType,
        BodyType body,
        DriveType drive
) {}
