package ru.CarDealership.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.order.state.OrderState;
import ru.CarDealership.domain.order.state.ReadyForIssueState;

@Getter
@SuperBuilder
public class StockOrder extends Order {
  private final Car car;

  @Override
  public OrderState getNextStateAfterPaid() {
    return new ReadyForIssueState();
  }
}
