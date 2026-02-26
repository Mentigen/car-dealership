package org.example.domain.order;

import lombok.Getter;
import org.example.domain.car.CarConfiguration;
import org.example.domain.order.state.AwaitingDeliveryState;
import org.example.domain.order.state.OrderState;
import org.example.domain.user.User;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class CustomOrder extends Order {
    private final CarConfiguration carConfiguration;

    public CustomOrder(OrderState state, UUID id, User client, User manager, BigDecimal price, CarConfiguration carConfiguration) {
        super(state, id, client, manager, price);
        this.carConfiguration = carConfiguration;
    }

    @Override
    public OrderState getNextStateAfterPaid() {
        return new AwaitingDeliveryState();
    }
}
