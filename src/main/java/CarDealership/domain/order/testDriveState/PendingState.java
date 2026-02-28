package CarDealership.domain.order.testDriveState;

import CarDealership.domain.order.TestDriveRequest;

public class PendingState implements TestDriveState {
  @Override
  public void approve(TestDriveRequest context) {
    context.changeState(new ApprovedState());
  }

  @Override
  public void reject(TestDriveRequest context) {
    context.changeState(new CancelledState());
  }

  @Override
  public void complete(TestDriveRequest context) {
    throw new IllegalStateException("Cannot complete pending request");
  }

  @Override
  public String getName() {
    return "PENDING";
  }
}
