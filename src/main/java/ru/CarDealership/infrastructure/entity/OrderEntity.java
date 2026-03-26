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
@Getter @Setter
@NoArgsConstructor
public class OrderEntity extends BaseEntity {
    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "manager_id", nullable = false)
    private UserEntity manager;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
}
