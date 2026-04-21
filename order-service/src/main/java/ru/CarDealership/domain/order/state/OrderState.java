package ru.CarDealership.domain.order.state;

import ru.CarDealership.domain.order.Order;

public interface OrderState {
    void next(Order context);
    void cancel(Order context);
    String getName(Order context);
}
