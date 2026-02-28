package CarDealership.domain.order.state;

import CarDealership.domain.order.Order;

public class ApprovedByWarehouseState implements OrderState {
  @Override
  public void next(Order context) {
    context.changeState(new AwaitingPaymentState());
  }

  @Override
  public void cancel(Order context) {
    context.changeState(new CancelledState());
  }

  @Override
  public String getName(Order context) {
    return "APPROVED_BY_WAREHOUSE";
  }
}
