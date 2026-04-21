package ru.CarDealership.domain.order.testDriveState;

import ru.CarDealership.domain.order.TestDriveRequest;

public class DoneState implements TestDriveState {
    @Override
    public void approve(TestDriveRequest context) {
        throw new IllegalStateException("Test drive is already done");
    }

    @Override
    public void reject(TestDriveRequest context) {
        throw new IllegalStateException("Test drive is already done");
    }

    @Override
    public void complete(TestDriveRequest context) {
        throw new IllegalStateException("Test drive is already done");
    }

    @Override
    public String getName() {
        return "DONE";
    }
}
