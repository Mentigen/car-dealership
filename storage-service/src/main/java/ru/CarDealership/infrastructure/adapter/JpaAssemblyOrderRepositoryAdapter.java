package ru.CarDealership.infrastructure.adapter;

import org.springframework.stereotype.Component;
import ru.CarDealership.domain.assembly.AssemblyOrder;
import ru.CarDealership.domain.assembly.AssemblyOrderRepository;
import ru.CarDealership.infrastructure.jpa.AssemblyOrderJpaRepository;
import ru.CarDealership.infrastructure.mappers.AssemblyOrderEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaAssemblyOrderRepositoryAdapter implements AssemblyOrderRepository {

    private final AssemblyOrderJpaRepository jpaRepository;
    private final AssemblyOrderEntityMapper mapper;

    public JpaAssemblyOrderRepositoryAdapter(AssemblyOrderJpaRepository jpaRepository, AssemblyOrderEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<AssemblyOrder> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AssemblyOrder> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public AssemblyOrder save(AssemblyOrder order) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(order)));
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRemoved(true);
            jpaRepository.save(e);
        });
    }

    @Override
    public boolean existsBySourceOrderId(UUID sourceOrderId) {
        return jpaRepository.existsBySourceOrderId(sourceOrderId);
    }
}
