package ru.CarDealership.service;

import ru.CarDealership.domain.car.Part;
import ru.CarDealership.domain.car.PartType;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.infrastructure.repository.InMemoryPartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PartServiceTest {

    private PartService partService;
    private InMemoryPartRepository partRepository;
    private Part part;

    @BeforeEach
    void setUp() {
        partRepository = new InMemoryPartRepository();
        partService = new PartService(partRepository);
        part = new Part(UUID.randomUUID(), PartType.WHEEL, new BigDecimal("100"), List.of());
    }

    @Test
    void testAddPart() {
        Part saved = partService.addPart(part);
        assertEquals(part, saved);
        assertEquals(part, partRepository.findById(part.getId()).orElse(null));
    }

    @Test
    void testUpdatePart() {
        partService.addPart(part);
        Part newData = new Part(null, PartType.WHEEL, new BigDecimal("150"), List.of());
        Part updated = partService.updatePart(part.getId(), newData);
        assertEquals(new BigDecimal("150"), updated.getPrice());
        assertEquals(part.getId(), updated.getId());
    }

    @Test
    void testUpdateNonExistentPart() {
        Part newData = new Part(null, PartType.WHEEL, new BigDecimal("150"), List.of());
        assertThrows(EntityNotFoundException.class, () -> partService.updatePart(UUID.randomUUID(), newData));
    }

    @Test
    void testDeletePart() {
        partService.addPart(part);
        partService.deletePart(part.getId());
        assertTrue(partRepository.findById(part.getId()).isEmpty());
    }

    @Test
    void testGetPartById() {
        partService.addPart(part);
        assertTrue(partService.getPartById(part.getId()).isPresent());
        assertTrue(partService.getPartById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void testGetAllParts() {
        partService.addPart(part);
        List<Part> all = partService.getAllParts();
        assertEquals(1, all.size());
    }
}
