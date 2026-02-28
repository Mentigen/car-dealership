package CarDealership.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import CarDealership.domain.car.Car;
import CarDealership.domain.order.state.OrderState;
import CarDealership.domain.order.state.ReadyForIssueState;

@Getter
@SuperBuilder
public class StockOrder extends Order {
  private final Car car;

  @Override
  public OrderState getNextStateAfterPaid() {
    return new ReadyForIssueState();
  }
}
