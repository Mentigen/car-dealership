package ru.CarDealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.CarDealership.domain.car.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarModelRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private CarModelRepository carModelRepository;

    @Test
    void findAll_returnsSeededModels() {
        List<CarModel> models = carModelRepository.findAll();
        assertFalse(models.isEmpty());
        assertTrue(models.size() >= 3);
    }

    @Test
    void findById_returnsExistingModel() {
        List<CarModel> all = carModelRepository.findAll();
        assertFalse(all.isEmpty());

        UUID firstId = all.get(0).getId();
        Optional<CarModel> found = carModelRepository.findById(firstId);
        assertTrue(found.isPresent());
        assertEquals(firstId, found.get().getId());
    }

    @Test
    void save_persistsNewModel() {
        CarModel model = new CarModel(
                null, "Hyundai", "Solaris",
                new Price(new BigDecimal("2000000")),
                new EnginePower(123),
                new EngineVolume(1.6),
                FuelType.PETROL, BodyType.SEDAN, DriveType.FWD
        );

        CarModel saved = carModelRepository.save(model);
        assertNotNull(saved.getId());

        Optional<CarModel> found = carModelRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Hyundai", found.get().getBrand());
    }
}
