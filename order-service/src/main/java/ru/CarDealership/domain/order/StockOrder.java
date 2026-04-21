package ru.CarDealership.domain.order;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.CarDealership.domain.order.state.OrderState;
import ru.CarDealership.domain.order.state.ReadyForIssueState;

import java.util.UUID;

@Getter
@SuperBuilder
public class StockOrder extends Order {
    private final UUID carId;

    @Override
    public OrderState getNextStateAfterPaid() {
        return new ReadyForIssueState();
    }
}
