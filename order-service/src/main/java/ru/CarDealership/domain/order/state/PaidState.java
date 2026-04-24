package ru.CarDealership.domain.order.state;

import ru.CarDealership.domain.order.Order;

public class PaidState implements OrderState {
    @Override
    public void next(Order context) {
        context.changeState(context.getNextStateAfterPaid());
    }

    @Override
    public void cancel(Order context) {
        context.changeState(new CancelledState());
    }

    @Override
    public String getName(Order context) {
        return "PAID";
    }
}
