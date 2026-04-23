package ru.CarDealership.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.CarDealership.config.RabbitMqConfig;
import ru.CarDealership.messaging.event.OrderApprovedEvent;
import ru.CarDealership.messaging.event.OrderRejectedEvent;
import ru.CarDealership.service.OrderService;

@Component
@AllArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitMqConfig.ORDER_APPROVED_QUEUE)
    @Transactional
    public void handleOrderApproved(OrderApprovedEvent event) {
        log.info("Received OrderApproved: orderId={}, traceId={}", event.orderId(), event.traceId());
        orderService.markAsReadyForIssue(event.orderId());
    }

    @RabbitListener(queues = RabbitMqConfig.ORDER_REJECTED_QUEUE)
    @Transactional
    public void handleOrderRejected(OrderRejectedEvent event) {
        log.info("Received OrderRejected: orderId={}, reason={}", event.orderId(), event.reason());
        orderService.markAsCancelled(event.orderId());
    }
}
