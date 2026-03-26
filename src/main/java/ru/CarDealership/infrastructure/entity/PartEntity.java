package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.CarDealership.domain.car.TransmissionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "part_kind", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PART")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartEntity extends BaseEntity {
    @Column(name = "type", nullable = false)
    private String type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column
    private String color;

    @Enumerated(EnumType.STRING)
    @Column
    private TransmissionType transmissionType;

    @ManyToMany
    @JoinTable(
            name = "part_compatible_models",
            joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "car_model_id")
    )
    private List<CarModelEntity> compatibleModels = new ArrayList<>();
}
