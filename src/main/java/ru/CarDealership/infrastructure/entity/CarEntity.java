package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cars")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(nullable = false)
    private CarConfigurationEntity carConfiguration;

    @Column(nullable = false)
    private boolean availableForTestDrive = false;
}
