package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_configurations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarConfigurationEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModelEntity carModel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "car_configuration_parts",
            joinColumns = @JoinColumn(name = "car_configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private List<PartEntity> parts = new ArrayList<>();
}
