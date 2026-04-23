package ru.CarDealership.infrastructure.adapter;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.car.Part;
import ru.CarDealership.domain.car.PartRepository;
import ru.CarDealership.infrastructure.jpa.PartJpaRepository;
import ru.CarDealership.infrastructure.mappers.PartEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaPartRepositoryAdapter implements PartRepository {

    private final PartJpaRepository jpaRepository;
    private final PartEntityMapper mapper;

    public JpaPartRepositoryAdapter(PartJpaRepository jpaRepository, PartEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Part> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Part> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Part save(Part part) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(part)));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRemoved(true);
            jpaRepository.save(e);
        });
    }
}
