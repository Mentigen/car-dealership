package ru.CarDealership.domain.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartRepository {
    Optional<Part> findById(UUID id);
    List<Part> findAll();
    Part save(Part part);
    void delete(UUID id);
}
