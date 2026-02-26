package org.example.domain.car;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CarFilter {
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private String brand;
    private String modelName;
    private BodyType bodyType;
    private FuelType fuelType;
    private Integer minPower;
    private Integer maxPower;
    private Double minEnginePower;
    private Double maxEnginePower;
    private Double minEngineVolume;
    private Double maxEngineVolume;
    private TransmissionType transmissionType;
    private String color;
    private DriveType driveType;
}
