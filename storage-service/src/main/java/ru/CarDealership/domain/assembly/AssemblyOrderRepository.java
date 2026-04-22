package ru.CarDealership.domain.assembly;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssemblyOrderRepository {
    Optional<AssemblyOrder> findById(UUID id);
    List<AssemblyOrder> findAll();
    AssemblyOrder save(AssemblyOrder order);
    void delete(UUID id);
    boolean existsBySourceOrderId(UUID sourceOrderId);
}
