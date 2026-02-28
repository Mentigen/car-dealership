package ru.CarDealership.domain.order.testDriveState;

import ru.CarDealership.domain.order.TestDriveRequest;

public class CancelledState implements TestDriveState {
  @Override
  public void approve(TestDriveRequest context) {
    throw new IllegalStateException("Cannot approve cancelled request");
  }

  @Override
  public void reject(TestDriveRequest context) {
    throw new IllegalStateException("Already cancelled");
  }

  @Override
  public void complete(TestDriveRequest context) {
    throw new IllegalStateException("Cannot complete cancelled request");
  }

  @Override
  public String getName() {
    return "CANCELLED";
  }
}
