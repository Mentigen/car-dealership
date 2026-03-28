package ru.CarDealership.infrastructure.adapter;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.TestDriveRequest;
import ru.CarDealership.domain.order.TestDriveRequestRepository;
import ru.CarDealership.infrastructure.jpa.TestDriveRequestJpaRepository;
import ru.CarDealership.infrastructure.mappers.TestDriveRequestEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaTestDriveRequestRepositoryAdapter implements TestDriveRequestRepository {

    private final TestDriveRequestJpaRepository jpaRepository;
    private final TestDriveRequestEntityMapper mapper;

    public JpaTestDriveRequestRepositoryAdapter(
            TestDriveRequestJpaRepository jpaRepository,
            TestDriveRequestEntityMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TestDriveRequest> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<TestDriveRequest> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<TestDriveRequest> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public TestDriveRequest save(TestDriveRequest testDriveRequest) {
        var entity = mapper.toEntity(testDriveRequest);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setRemoved(true);
            jpaRepository.save(entity);
        });
    }
}