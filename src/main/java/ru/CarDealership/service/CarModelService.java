package ru.CarDealership.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.CarDealership.domain.car.CarModel;
import ru.CarDealership.domain.car.CarModelRepository;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarModelService {
    private final CarModelRepository carModelRepository;

    public List<CarModel> findAll() {
        return carModelRepository.findAll();
    }

    public CarModel findById(UUID id) {
        return carModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car model not found"));
    }

    public CarModel save(CarModel carModel) {
        return carModelRepository.save(carModel);
    }

    public void delete(UUID id) {
        carModelRepository.delete(id);
    }
}
