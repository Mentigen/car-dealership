package ru.CarDealership.domain.order.state;

import ru.CarDealership.domain.order.Order;

public class AwaitingDeliveryState implements OrderState {
  @Override
  public void next(Order context) {
    context.changeState(new ReadyForIssueState());
  }

  @Override
  public void cancel(Order context) {
    throw new IllegalStateException("Cannot cancel on this step.");
  }

  @Override
  public String getName(Order context) {
    return "AWAITING_DELIVERY";
  }
}
