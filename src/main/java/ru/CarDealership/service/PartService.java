package ru.CarDealership.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.domain.car.Part;
import ru.CarDealership.domain.car.PartRepository;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class PartService {
  private final PartRepository partRepository;

  public Part addPart(Part part) {
    return partRepository.save(part);
  }

  public Part updatePart(UUID id, Part newPartData) {
    Part part =
        partRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Part not found"));

    newPartData.setId(id);

    return partRepository.save(newPartData);
  }

  public void deletePart(UUID id) {
    partRepository.delete(id);
  }

  @Transactional(readOnly = true)
  public Optional<Part> getPartById(UUID id) {
    return partRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public List<Part> getAllParts() {
    return partRepository.findAll();
  }
}
