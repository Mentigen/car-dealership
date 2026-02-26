package org.example.domain.order.state;

import org.example.domain.order.Order;

public class CreatedCustomState implements OrderState {
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
        return "CREATED_CUSTOM";
    }
}
