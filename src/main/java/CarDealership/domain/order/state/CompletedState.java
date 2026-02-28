package CarDealership.domain.order.state;

import CarDealership.domain.order.Order;

public class CompletedState implements OrderState {
  @Override
  public void next(Order context) {
    throw new IllegalStateException("Order is already completed.");
  }

  @Override
  public void cancel(Order context) {
    throw new IllegalStateException("Order is already completed.");
  }

  @Override
  public String getName(Order context) {
    return "COMPLETED";
  }
}
