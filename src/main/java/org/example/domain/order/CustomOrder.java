package org.example.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.example.domain.car.CarConfiguration;
import org.example.domain.order.state.AwaitingDeliveryState;
import org.example.domain.order.state.OrderState;
import org.example.domain.user.User;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@SuperBuilder
public class CustomOrder extends Order {
  private final CarConfiguration carConfiguration;

  @Override
  public OrderState getNextStateAfterPaid() {
    return new AwaitingDeliveryState();
  }
}
