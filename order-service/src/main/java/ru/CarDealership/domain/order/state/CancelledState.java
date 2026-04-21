package ru.CarDealership.domain.order.state;

import ru.CarDealership.domain.order.Order;

public class CancelledState implements OrderState {
    @Override
    public void next(Order context) {
        throw new IllegalStateException("Cannot advance cancelled order");
    }

    @Override
    public void cancel(Order context) {
        throw new IllegalStateException("Order is already cancelled");
    }

    @Override
    public String getName(Order context) {
        return "CANCELLED";
    }
}
