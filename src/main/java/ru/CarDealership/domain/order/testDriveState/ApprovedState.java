package ru.CarDealership.domain.order.testDriveState;

import ru.CarDealership.domain.order.TestDriveRequest;

public class ApprovedState implements TestDriveState {
  @Override
  public void approve(TestDriveRequest context) {
    throw new IllegalStateException("Already approved");
  }

  @Override
  public void reject(TestDriveRequest context) {
    context.changeState(new CancelledState());
  }

  @Override
  public void complete(TestDriveRequest context) {
    context.changeState(new DoneState());
  }

  @Override
  public String getName() {
    return "APPROVED";
  }
}
