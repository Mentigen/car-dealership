package org.example.infrastructure.repository;

import org.example.domain.car.Part;
import org.example.domain.car.PartRepository;

import java.util.*;

public class InMemoryPartRepository implements PartRepository {
    private final HashMap<UUID, Part> storage =  new HashMap<>();

    @Override
    public Optional<Part> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Part> findAll() {
        return new ArrayList<Part>(storage.values());
    }

    @Override
    public Part save(Part part) {
        if (part.getId() == null) {
            part.setId(UUID.randomUUID());
        }
        storage.put(part.getId(), part);

        return part;
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}
