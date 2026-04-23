package ru.CarDealership.infrastructure.adapter;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.CarModel;
import ru.CarDealership.domain.car.CarModelRepository;
import ru.CarDealership.infrastructure.jpa.CarModelJpaRepository;
import ru.CarDealership.infrastructure.mappers.CarModelEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaCarModelRepositoryAdapter implements CarModelRepository {

    private final CarModelJpaRepository jpaRepository;
    private final CarModelEntityMapper mapper;

    public JpaCarModelRepositoryAdapter(CarModelJpaRepository jpaRepository, CarModelEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CarModel> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<CarModel> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public CarModel save(CarModel model) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(model)));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRemoved(true);
            jpaRepository.save(e);
        });
    }
}
