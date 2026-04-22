package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@DiscriminatorValue("CUSTOM")
@Getter
@NoArgsConstructor
public class CustomOrderEntity extends OrderEntity {
    @Setter
    @Column(name = "car_configuration_id")
    private UUID carConfigurationId;
}
