package ru.CarDealership.infrastructure.repository;

import ru.CarDealership.domain.car.Part;
import ru.CarDealership.domain.car.PartRepository;

import java.util.*;

public class InMemoryPartRepository implements PartRepository {
    private final Map<UUID, Part> storage = new HashMap<>();

    @Override
    public Optional<Part> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Part> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Part save(Part part) {
        storage.put(part.getId(), part);
        return part;
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}
