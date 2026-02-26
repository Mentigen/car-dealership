package org.example.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.car.Car;
import org.example.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class TestDriveRequest {
    private final UUID id;
    private final User client;
    private final Car car;
    private final LocalDateTime startTime;
    private final TestDriveStatus status;
}
