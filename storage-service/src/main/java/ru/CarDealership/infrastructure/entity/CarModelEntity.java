package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.CarDealership.domain.car.BodyType;
import ru.CarDealership.domain.car.DriveType;
import ru.CarDealership.domain.car.FuelType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_models")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarModelEntity extends BaseEntity {
    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int enginePower;

    @Column(nullable = false)
    private double engineVolume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BodyType body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriveType drive;

    @ManyToMany(mappedBy = "compatibleModels")
    private List<PartEntity> compatibleParts = new ArrayList<>();
}
