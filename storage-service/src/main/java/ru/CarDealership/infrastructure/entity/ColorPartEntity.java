package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("COLOR_PART")
@NoArgsConstructor
public class ColorPartEntity extends PartEntity {
}
