package ru.CarDealership.domain.car;

import lombok.*;

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
    private Price price;
    private EnginePower enginePower;
    private EngineVolume engineVolume;
    private FuelType fuel;
    private BodyType body;
    private DriveType drive;
}
