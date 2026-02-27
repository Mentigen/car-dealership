package org.example.domain.order.testDriveState;

import org.example.domain.order.TestDriveRequest;

public class DoneState implements TestDriveState {
  @Override
  public void approve(TestDriveRequest context) {
    throw new IllegalStateException("Cannot approve finished request");
  }

  @Override
  public void reject(TestDriveRequest context) {
    throw new IllegalStateException("Cannot reject finished request");
  }

  @Override
  public void complete(TestDriveRequest context) {
    throw new IllegalStateException("Already completed");
  }

  @Override
  public String getName() {
    return "DONE";
  }
}
