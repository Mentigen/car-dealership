package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "order_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor
public class OrderEntity extends BaseEntity {
    @Setter
    @Column(nullable = false)
    private String status;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "manager_id", nullable = false)
    private UserEntity manager;

    @Setter
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
}
