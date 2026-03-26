package ru.CarDealership.api.dto;

import lombok.Data;
import ru.CarDealership.domain.car.BodyType;
import ru.CarDealership.domain.car.DriveType;
import ru.CarDealership.domain.car.FuelType;
import ru.CarDealership.domain.car.TransmissionType;

import java.math.BigDecimal;

@Data
public class CarFilterRequest {
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private String brand;
    private String modelName;
    private BodyType bodyType;
    private FuelType fuelType;
    private Integer minPower;
    private Integer maxPower;
    private Double minEngineVolume;
    private Double maxEngineVolume;
    private TransmissionType transmissionType;
    private String color;
    private DriveType driveType;
}
