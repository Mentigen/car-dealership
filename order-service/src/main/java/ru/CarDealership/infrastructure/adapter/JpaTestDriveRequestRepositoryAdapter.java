package ru.CarDealership.infrastructure.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.CarDealership.domain.order.TestDriveRequest;
import ru.CarDealership.domain.order.TestDriveRequestRepository;
import ru.CarDealership.infrastructure.jpa.TestDriveRequestJpaRepository;
import ru.CarDealership.infrastructure.mappers.TestDriveRequestEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaTestDriveRequestRepositoryAdapter implements TestDriveRequestRepository {
    private final TestDriveRequestJpaRepository jpaRepository;
    private final TestDriveRequestEntityMapper mapper;

    @Override
    public Optional<TestDriveRequest> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<TestDriveRequest> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public TestDriveRequest save(TestDriveRequest request) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(request)));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRemoved(true);
            jpaRepository.save(e);
        });
    }
}
