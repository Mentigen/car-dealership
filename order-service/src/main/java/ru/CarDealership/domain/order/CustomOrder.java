package ru.CarDealership.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.CarDealership.domain.order.state.AwaitingDeliveryState;
import ru.CarDealership.domain.order.state.OrderState;

import java.util.UUID;

@Getter
@SuperBuilder
public class CustomOrder extends Order {
    private final UUID carConfigurationId;

    @Override
    public OrderState getNextStateAfterPaid() {
        return new AwaitingDeliveryState();
    }
}
