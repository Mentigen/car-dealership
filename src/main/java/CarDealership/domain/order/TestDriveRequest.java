package CarDealership.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import CarDealership.domain.car.Car;
import CarDealership.domain.order.testDriveState.TestDriveState;
import CarDealership.domain.user.User;

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
