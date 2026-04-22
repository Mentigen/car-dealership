package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@DiscriminatorValue("STOCK")
@Getter
@NoArgsConstructor
public class StockOrderEntity extends OrderEntity {
    @Setter
    @Column(name = "car_id")
    private UUID carId;
}
