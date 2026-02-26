package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.car.Car;
import org.example.domain.car.CarRepository;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.domain.order.TestDriveRequest;
import org.example.domain.order.TestDriveRequestRepository;
import org.example.domain.order.TestDriveStatus;
import org.example.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TestDriveService {
  private final CarRepository carRepository;
  private final TestDriveRequestRepository requestRepository;

  public TestDriveRequest requestTestDrive(User client, Car car, LocalDateTime time) {
    TestDriveRequest request =
        new TestDriveRequest(UUID.randomUUID(), client, car, time, TestDriveStatus.PENDING);
    requestRepository.save(request);

    return request;
  }

  public void approveRequest(UUID id) {
    TestDriveRequest request =
        requestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Request not found"));

    if (request.getStatus() != TestDriveStatus.PENDING) {
      throw new IllegalStateException("Only PENDING requests can be approved");
    }

    TestDriveRequest approvedRequest =
        new TestDriveRequest(
            request.getId(),
            request.getClient(),
            request.getCar(),
            request.getStartTime(),
            TestDriveStatus.APPROVED);

    requestRepository.save(approvedRequest);
  }

  public void rejectRequest(UUID id) {
    TestDriveRequest request =
        requestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Request not found"));

    if (request.getStatus() == TestDriveStatus.DONE) {
      throw new IllegalStateException("DONE requests cannot be cancelled");
    }

    TestDriveRequest cancelledRequest =
        new TestDriveRequest(
            request.getId(),
            request.getClient(),
            request.getCar(),
            request.getStartTime(),
            TestDriveStatus.CANCELLED);

    requestRepository.save(cancelledRequest);
  }

  public void completeRequest(UUID id) {
    TestDriveRequest request =
        requestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Request not found"));

    if (request.getStatus() != TestDriveStatus.APPROVED) {
      throw new IllegalStateException("Only APPROVED requests can be complete");
    }

    TestDriveRequest completeRequest =
        new TestDriveRequest(
            request.getId(),
            request.getClient(),
            request.getCar(),
            request.getStartTime(),
            TestDriveStatus.DONE);

    requestRepository.save(completeRequest);
  }

  public List<Car> getTestDriveCars() {
    return carRepository.findTestDriveCars();
  }

  public void addCarToTestDrive(Car car) {
    carRepository.addTestDriveCar(car.getId());
  }

  public void removeCarFromTestDrive(Car car) {
    carRepository.removeTestDriveCar(car.getId());
  }
}
