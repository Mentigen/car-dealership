package ru.CarDealership.domain.order.state;

import ru.CarDealership.domain.order.Order;

public class ReadyForIssueState implements OrderState {
    @Override
    public void next(Order context) {
        context.changeState(new CompletedState());
    }

    @Override
    public void cancel(Order context) {
        throw new IllegalStateException("Cannot cancel on this step");
    }

    @Override
    public String getName(Order context) {
        return "READY_FOR_ISSUE";
    }
}
