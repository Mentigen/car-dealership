package ru.CarDealership.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.config.RabbitMqConfig;
import ru.CarDealership.domain.assembly.AssemblyOrder;
import ru.CarDealership.domain.assembly.AssemblyOrderRepository;
import ru.CarDealership.domain.assembly.AssemblyOrderStatus;
import ru.CarDealership.messaging.event.OrderSentForApprovalEvent;

@Component
@AllArgsConstructor
@Slf4j
public class AssemblyOrderEventConsumer {

    private final AssemblyOrderRepository assemblyOrderRepository;

    @RabbitListener(queues = RabbitMqConfig.ORDER_SENT_FOR_APPROVAL_QUEUE)
    @Transactional
    public void handleOrderSentForApproval(OrderSentForApprovalEvent event) {
        log.info("Received OrderSentForApproval: orderId={}, traceId={}", event.orderId(), event.traceId());

        if (assemblyOrderRepository.existsBySourceOrderId(event.orderId())) {
            log.warn("AssemblyOrder already exists for orderId={}, skipping", event.orderId());
            return;
        }

        AssemblyOrder order = AssemblyOrder.builder()
                .sourceOrderId(event.orderId())
                .traceId(event.traceId())
                .orderType(event.orderType())
                .carId(event.carId())
                .carConfigurationId(event.carConfigurationId())
                .status(AssemblyOrderStatus.CREATED)
                .build();

        assemblyOrderRepository.save(order);
        log.info("Created AssemblyOrder for orderId={}", event.orderId());
    }
}
