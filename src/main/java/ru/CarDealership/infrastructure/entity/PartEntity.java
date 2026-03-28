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
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartEntity extends BaseEntity {
    @Setter
    @Column(name = "type", nullable = false)
    private String type;

    @Setter
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Setter
    @Column
    private String color;

    @Setter
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
