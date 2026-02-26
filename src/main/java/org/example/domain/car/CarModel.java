package org.example.domain.car;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarModel {
    private UUID id;
    private String brand;
    private String modelName;
    private BigDecimal price;
    private int enginePower;
    private double engineVolume;
    private FuelType fuel;
    private BodyType body;
    private DriveType drive;
}