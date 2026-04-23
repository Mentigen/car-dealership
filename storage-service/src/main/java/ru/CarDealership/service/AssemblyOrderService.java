package ru.CarDealership.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.domain.assembly.AssemblyOrder;
import ru.CarDealership.domain.assembly.AssemblyOrderRepository;
import ru.CarDealership.domain.assembly.AssemblyOrderStatus;
import ru.CarDealership.domain.exceptions.EntityNotFoundException;
import ru.CarDealership.messaging.AssemblyOrderEventProducer;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AssemblyOrderService {

    private final AssemblyOrderRepository assemblyOrderRepository;
    private final AssemblyOrderEventProducer eventProducer;

    public AssemblyOrder create(AssemblyOrder order) {
        return assemblyOrderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<AssemblyOrder> findAll() {
        return assemblyOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AssemblyOrder findById(UUID id) {
        return assemblyOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assembly order not found"));
    }

    public AssemblyOrder updateStatus(UUID id, AssemblyOrderStatus newStatus, String failReason) {
        AssemblyOrder order = assemblyOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assembly order not found"));
        order.setStatus(newStatus);
        order.setFailReason(failReason);
        AssemblyOrder saved = assemblyOrderRepository.save(order);

        if (newStatus == AssemblyOrderStatus.ASSEMBLED) {
            eventProducer.publishApproved(saved);
        } else if (newStatus == AssemblyOrderStatus.FAIL) {
            eventProducer.publishRejected(saved, failReason != null ? failReason : "Assembly failed");
        }

        return saved;
    }

    public void delete(UUID id) {
        assemblyOrderRepository.delete(id);
    }
}
