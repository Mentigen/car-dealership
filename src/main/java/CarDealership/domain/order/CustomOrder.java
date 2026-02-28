package CarDealership.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import CarDealership.domain.car.CarConfiguration;
import CarDealership.domain.order.state.AwaitingDeliveryState;
import CarDealership.domain.order.state.OrderState;

@Getter
@SuperBuilder
public class CustomOrder extends Order {
  private final CarConfiguration carConfiguration;

  @Override
  public OrderState getNextStateAfterPaid() {
    return new AwaitingDeliveryState();
  }
}
