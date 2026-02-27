package org.example.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.example.domain.car.Car;
import org.example.domain.order.state.OrderState;
import org.example.domain.order.state.ReadyForIssueState;
import org.example.domain.user.User;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@SuperBuilder
public class StockOrder extends Order {
  private final Car car;

  @Override
  public OrderState getNextStateAfterPaid() {
    return new ReadyForIssueState();
  }
}
