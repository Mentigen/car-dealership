package ru.CarDealership.domain.order.testDriveState;

import ru.CarDealership.domain.order.TestDriveRequest;

public interface TestDriveState {
  void approve(TestDriveRequest context);

  void reject(TestDriveRequest context);

  void complete(TestDriveRequest context);

  String getName();
}
