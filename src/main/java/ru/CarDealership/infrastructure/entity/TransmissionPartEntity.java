package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("TRANSMISSION_PART")
@NoArgsConstructor
public class TransmissionPartEntity extends PartEntity {
}
