package ru.CarDealership.domain.car;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    Optional<Car> findById(UUID id);
    List<Car> findAll();
    Page<Car> findAll(Pageable pageable);
    Optional<CarConfiguration> findByConfigurationId(UUID configId);
    Page<Car> findFiltered(CarFilter filter, Pageable pageable);
    Car save(Car car);
    void delete(UUID id);
    void addTestDriveCar(UUID carId);
    void removeTestDriveCar(UUID carId);
    List<Car> findTestDriveCars();
}
