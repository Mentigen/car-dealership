package ru.CarDealership.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.config.RabbitMqConfig;
import ru.CarDealership.infrastructure.outbox.OutboxEvent;
import ru.CarDealership.infrastructure.outbox.OutboxEventRepository;
import ru.CarDealership.messaging.event.OrderSentForApprovalEvent;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxEventRepository.findBySentFalse();
        for (OutboxEvent event : events) {
            try {
                OrderSentForApprovalEvent message = new OrderSentForApprovalEvent(
                        event.getOrderId(),
                        event.getTraceId(),
                        event.getOrderType(),
                        event.getCarId(),
                        event.getCarConfigurationId(),
                        Instant.now()
                );
                rabbitTemplate.convertAndSend(
                        RabbitMqConfig.EXCHANGE,
                        RabbitMqConfig.ORDER_SENT_FOR_APPROVAL_QUEUE,
                        message
                );
                event.setSent(true);
                event.setSentAt(Instant.now());
                outboxEventRepository.save(event);
                log.info("Published OrderSentForApproval for orderId={}", event.getOrderId());
            } catch (Exception e) {
                log.error("Failed to publish outbox event id={}", event.getId(), e);
            }
        }
    }
}
