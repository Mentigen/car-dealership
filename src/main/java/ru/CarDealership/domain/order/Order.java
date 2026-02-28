package ru.CarDealership.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.CarDealership.domain.order.state.OrderState;
import ru.CarDealership.domain.user.User;

import java.math.BigDecimal;
import java.util.UUID;

@SuperBuilder
@Getter
public abstract class Order {
  private OrderState state;
  private final UUID id;
  private final User client;
  private final User manager;
  private final BigDecimal price;

  public void changeState(OrderState newState) {
    this.state = newState;
  }

  public void next() {
    this.state.next(this);
  }

  public void cancel() {
    this.state.cancel(this);
  }

  public abstract OrderState getNextStateAfterPaid();
}
