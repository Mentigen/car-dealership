package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_drive_requests")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestDriveRequestEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private String status;
}
