package org.example.domain.order;

import lombok.Getter;
import org.example.domain.car.Car;
import org.example.domain.order.state.OrderState;
import org.example.domain.order.state.ReadyForIssueState;
import org.example.domain.user.User;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class StockOrder extends Order {
  private final Car car;

  public StockOrder(
      OrderState state, UUID id, User client, User manager, BigDecimal price, Car car) {
    super(state, id, client, manager, price);
    this.car = car;
  }

  @Override
  public OrderState getNextStateAfterPaid() {
    return new ReadyForIssueState();
  }
}
