package ru.CarDealership.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.CarDealership.domain.car.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryCarRepository implements CarRepository {
    private final Map<UUID, Car> storage = new HashMap<>();
    private final Set<UUID> testDriveCars = new HashSet<>();

    @Override
    public Optional<Car> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Page<Car> findAll(Pageable pageable) {
        List<Car> all = findAll();
        return toPage(all, pageable);
    }

    @Override
    public Optional<CarConfiguration> findByConfigurationId(UUID configId) {
        return storage.values().stream()
                .map(Car::getCarConfiguration)
                .filter(c -> configId.equals(c.getId()))
                .findFirst();
    }

    @Override
    public Page<Car> findFiltered(CarFilter filter, Pageable pageable) {
        List<Car> filtered = storage.values().stream()
                .filter(car -> matches(car, filter))
                .collect(Collectors.toList());
        return toPage(filtered, pageable);
    }

    @Override
    public Car save(Car car) {
        storage.put(car.getId(), car);
        return car;
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }

    @Override
    public void addTestDriveCar(UUID carId) {
        testDriveCars.add(carId);
    }

    @Override
    public void removeTestDriveCar(UUID carId) {
        testDriveCars.remove(carId);
    }

    @Override
    public List<Car> findTestDriveCars() {
        return testDriveCars.stream()
                .map(storage::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean matches(Car car, CarFilter filter) {
        CarConfiguration cfg = car.getCarConfiguration();
        CarModel model = cfg.getModel();

        if (filter.getBrand() != null && !filter.getBrand().equalsIgnoreCase(model.getBrand())) return false;
        if (filter.getModelName() != null && !filter.getModelName().equalsIgnoreCase(model.getModelName())) return false;
        if (filter.getMinPower() != null && model.getEnginePower() != null
                && model.getEnginePower().getValue() < filter.getMinPower()) return false;
        if (filter.getMinPrice() != null && car.getPrice().compareTo(filter.getMinPrice()) < 0) return false;
        if (filter.getMaxPrice() != null && car.getPrice().compareTo(filter.getMaxPrice()) > 0) return false;

        if (filter.getTransmissionType() != null) {
            boolean hasTransmission = cfg.getParts().stream()
                    .filter(p -> p instanceof TransmissionPart)
                    .map(p -> (TransmissionPart) p)
                    .anyMatch(tp -> filter.getTransmissionType().equals(tp.getTransmissionType()));
            if (!hasTransmission) return false;
        }

        if (filter.getColor() != null) {
            boolean hasColor = cfg.getParts().stream()
                    .filter(p -> p instanceof ColorPart)
                    .map(p -> (ColorPart) p)
                    .anyMatch(cp -> filter.getColor().equalsIgnoreCase(cp.getColor()));
            if (!hasColor) return false;
        }

        return true;
    }

    private Page<Car> toPage(List<Car> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        List<Car> page = start > list.size() ? List.of() : list.subList(start, end);
        return new PageImpl<>(page, pageable, list.size());
    }
}
