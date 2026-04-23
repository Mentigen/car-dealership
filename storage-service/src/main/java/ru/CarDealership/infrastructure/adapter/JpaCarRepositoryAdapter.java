package ru.CarDealership.infrastructure.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.Car;
import ru.CarDealership.domain.car.CarConfiguration;
import ru.CarDealership.domain.car.CarFilter;
import ru.CarDealership.domain.car.CarRepository;
import ru.CarDealership.infrastructure.entity.CarConfigurationEntity;
import ru.CarDealership.infrastructure.entity.CarEntity;
import ru.CarDealership.infrastructure.jpa.CarJpaRepository;
import ru.CarDealership.infrastructure.mappers.CarEntityMapper;
import ru.CarDealership.infrastructure.specifications.CarEntitySpecifications;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaCarRepositoryAdapter implements CarRepository {

    private final CarJpaRepository jpaRepository;
    private final CarEntityMapper mapper;

    public JpaCarRepositoryAdapter(CarJpaRepository jpaRepository, CarEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Car> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Car> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<Car> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<CarConfiguration> findByConfigurationId(UUID configId) {
        return jpaRepository.findByCarConfiguration_Id(configId)
                .map(mapper::toDomain)
                .map(Car::getCarConfiguration);
    }

    @Override
    public Page<Car> findFiltered(CarFilter filter, Pageable pageable) {
        return jpaRepository.findAll(CarEntitySpecifications.fromFilter(filter), pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Car save(Car car) {
        var entity = new CarEntity();
        entity.setId(car.getId());
        var configEntity = new CarConfigurationEntity();
        configEntity.setId(car.getCarConfiguration().getId());
        entity.setCarConfiguration(configEntity);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRemoved(true);
            jpaRepository.save(e);
        });
    }

    @Override
    public void addTestDriveCar(UUID carId) {
        jpaRepository.findById(carId).ifPresent(car -> {
            car.setAvailableForTestDrive(true);
            jpaRepository.save(car);
        });
    }

    @Override
    public void removeTestDriveCar(UUID carId) {
        jpaRepository.findById(carId).ifPresent(car -> {
            car.setAvailableForTestDrive(false);
            jpaRepository.save(car);
        });
    }

    @Override
    public List<Car> findTestDriveCars() {
        return jpaRepository.findByAvailableForTestDriveTrue().stream().map(mapper::toDomain).toList();
    }
}
