package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cars")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarEntity extends BaseEntity {
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(nullable = false)
    private CarConfigurationEntity carConfiguration;

    @Setter
    @Column(nullable = false)
    private boolean availableForTestDrive = false;
}
