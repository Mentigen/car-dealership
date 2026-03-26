package ru.CarDealership.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.CarDealership.domain.car.BodyType;
import ru.CarDealership.domain.car.DriveType;
import ru.CarDealership.domain.car.FuelType;

import java.math.BigDecimal;

@Data
public class CarModelRequest {
    @NotBlank
    private String brand;
    @NotBlank
    private String modelName;
    @NotNull @Positive
    private BigDecimal price;
    @Positive
    private int enginePower;
    @Positive
    private double engineVolume;
    @NotNull
    private FuelType fuelType;
    @NotNull
    private BodyType body;
    @NotNull
    private DriveType drive;
}
