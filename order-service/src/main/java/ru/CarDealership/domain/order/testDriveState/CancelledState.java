package ru.CarDealership.domain.order.testDriveState;

import ru.CarDealership.domain.order.TestDriveRequest;

public class CancelledState implements TestDriveState {
    @Override
    public void approve(TestDriveRequest context) {
        throw new IllegalStateException("Cannot approve cancelled test drive");
    }

    @Override
    public void reject(TestDriveRequest context) {
        throw new IllegalStateException("Test drive is already cancelled");
    }

    @Override
    public void complete(TestDriveRequest context) {
        throw new IllegalStateException("Cannot complete cancelled test drive");
    }

    @Override
    public String getName() {
        return "CANCELLED";
    }
}
