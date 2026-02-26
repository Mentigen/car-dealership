package org.example.domain.order.state;

import org.example.domain.order.Order;

public class CreatedStockState implements OrderState {
    @Override
    public void next(Order context) {
        context.changeState(new ApprovedByManagerState());
    }

    @Override
    public void cancel(Order context) {
        context.changeState(new CancelledState());
    }

    @Override
    public String getName(Order context) {
        return "CREATED_STOCK";
    }
}