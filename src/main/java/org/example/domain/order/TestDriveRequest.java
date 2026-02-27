package org.example.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.car.Car;
import org.example.domain.order.testDriveState.TestDriveState;
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
  private TestDriveState state;

  public void changeState(TestDriveState newState) {
    this.state = newState;
  }

  public void approve() {
    state.approve(this);
  }

  public void reject() {
    state.reject(this);
  }

  public void complete() {
    state.complete(this);
  }
}
