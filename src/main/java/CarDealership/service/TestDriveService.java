package CarDealership.service;

import lombok.AllArgsConstructor;
import CarDealership.domain.car.Car;
import CarDealership.domain.car.CarRepository;
import CarDealership.domain.exceptions.EntityNotFoundException;
import CarDealership.domain.order.TestDriveRequest;
import CarDealership.domain.order.TestDriveRequestRepository;
import CarDealership.domain.order.testDriveState.PendingState;
import CarDealership.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TestDriveService {
  private final CarRepository carRepository;
  private final TestDriveRequestRepository requestRepository;

  public TestDriveRequest requestTestDrive(User client, Car car, LocalDateTime time) {
    TestDriveRequest request =
        new TestDriveRequest(UUID.randomUUID(), client, car, time, new PendingState());
    requestRepository.save(request);

    return request;
  }

  public void approveRequest(UUID id) {
    TestDriveRequest request =
        requestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Request not found"));

    request.approve();
    requestRepository.save(request);
  }

  public void rejectRequest(UUID id) {
    TestDriveRequest request =
        requestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Request not found"));

    request.reject();
    requestRepository.save(request);
  }

  public void completeRequest(UUID id) {
    TestDriveRequest request =
        requestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Request not found"));

    request.complete();
    requestRepository.save(request);
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
