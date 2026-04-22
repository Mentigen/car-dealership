package ru.CarDealership.domain.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarModelRepository {
    Optional<CarModel> findById(UUID id);
    List<CarModel> findAll();
    CarModel save(CarModel model);
    void delete(UUID id);
}
