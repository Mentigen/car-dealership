package ru.CarDealership.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.CarDealership.config.RabbitMqConfig;
import ru.CarDealership.domain.assembly.AssemblyOrder;
import ru.CarDealership.messaging.event.OrderApprovedEvent;
import ru.CarDealership.messaging.event.OrderRejectedEvent;

import java.time.Instant;

@Component
@AllArgsConstructor
@Slf4j
public class AssemblyOrderEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishApproved(AssemblyOrder order) {
        var event = new OrderApprovedEvent(
                order.getSourceOrderId(),
                order.getTraceId(),
                order.getId(),
                Instant.now()
        );
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ORDER_APPROVED_QUEUE, event);
        log.info("Published OrderApproved for sourceOrderId={}", order.getSourceOrderId());
    }

    public void publishRejected(AssemblyOrder order, String reason) {
        var event = new OrderRejectedEvent(
                order.getSourceOrderId(),
                order.getTraceId(),
                reason,
                Instant.now()
        );
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ORDER_REJECTED_QUEUE, event);
        log.info("Published OrderRejected for sourceOrderId={}, reason={}", order.getSourceOrderId(), reason);
    }
}
