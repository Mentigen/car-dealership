package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.car.Part;
import org.example.domain.car.PartRepository;
import org.example.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PartService {
    private final PartRepository partRepository;

    public Part addPart(Part part) {
        return partRepository.save(part);
    }

    public Part updatePart(UUID id, Part newPartData) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part not found"));

        newPartData.setId(id);

        return partRepository.save(newPartData);
    }

    public void deletePart(UUID id) {
        partRepository.delete(id);
    }

    public Optional<Part> getPartById(UUID id) {
        return partRepository.findById(id);
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

}
