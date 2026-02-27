package org.example.domain.order.testDriveState;

import org.example.domain.order.TestDriveRequest;

public interface TestDriveState {
  void approve(TestDriveRequest context);

  void reject(TestDriveRequest context);

  void complete(TestDriveRequest context);

  String getName();
}
