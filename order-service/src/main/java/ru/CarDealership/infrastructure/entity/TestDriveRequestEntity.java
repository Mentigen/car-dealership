package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_drive_requests")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestDriveRequestEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @Column(name = "car_id", nullable = false)
    private UUID carId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private String status;
}
