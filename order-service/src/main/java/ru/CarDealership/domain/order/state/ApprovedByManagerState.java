package ru.CarDealership.domain.order.state;

import ru.CarDealership.domain.order.Order;

public class ApprovedByManagerState implements OrderState {
    @Override
    public void next(Order context) {
        context.changeState(new ApprovedByWarehouseState());
    }

    @Override
    public void cancel(Order context) {
        context.changeState(new CancelledState());
    }

    @Override
    public String getName(Order context) {
        return "APPROVED_BY_MANAGER";
    }
}
