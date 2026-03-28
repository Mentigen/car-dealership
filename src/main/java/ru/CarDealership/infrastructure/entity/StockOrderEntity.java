package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("STOCK")
@Getter
@NoArgsConstructor
public class StockOrderEntity extends OrderEntity {
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "car_id")
    private CarEntity car;
}
