package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_drive_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestDriveRequestEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private String status;
}
