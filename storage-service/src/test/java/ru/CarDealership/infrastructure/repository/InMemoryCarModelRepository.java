package ru.CarDealership.infrastructure.repository;

import ru.CarDealership.domain.car.CarModel;
import ru.CarDealership.domain.car.CarModelRepository;

import java.util.*;

public class InMemoryCarModelRepository implements CarModelRepository {
    private final Map<UUID, CarModel> storage = new HashMap<>();

    @Override
    public Optional<CarModel> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<CarModel> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public CarModel save(CarModel model) {
        storage.put(model.getId(), model);
        return model;
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}
