package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CUSTOM")
@Getter @Setter
@NoArgsConstructor
public class CustomOrderEntity extends OrderEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "car_configuration_id")
    private CarConfigurationEntity carConfiguration;
}
