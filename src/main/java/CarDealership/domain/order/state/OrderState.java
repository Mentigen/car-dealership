package CarDealership.domain.order.state;

import CarDealership.domain.order.Order;

public interface OrderState {
  public void next(Order context);

  public void cancel(Order context);

  public String getName(Order context);
}
