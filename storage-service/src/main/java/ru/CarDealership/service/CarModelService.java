package ru.CarDealership.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.domain.car.CarModel;
import ru.CarDealership.domain.car.CarModelRepository;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class CarModelService {

    private final CarModelRepository carModelRepository;

    @Transactional(readOnly = true)
    public List<CarModel> findAll() {
        return carModelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CarModel findById(UUID id) {
        return carModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car model not found"));
    }

    public CarModel save(CarModel model) {
        return carModelRepository.save(model);
    }

    public void delete(UUID id) {
        carModelRepository.delete(id);
    }
}
