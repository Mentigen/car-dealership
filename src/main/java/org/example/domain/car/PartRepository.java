package org.example.domain.car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartRepository {
    public Optional<Part> findById(UUID id);
    public List<Part> findAll();
    public Part save(Part part);
    public void delete(UUID id);
}
