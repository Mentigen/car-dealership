package ru.CarDealership.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "car-dealership";

    public static final String ORDER_SENT_FOR_APPROVAL_QUEUE = "order.sent-for-approval";
    public static final String ORDER_APPROVED_QUEUE = "order.approved";
    public static final String ORDER_REJECTED_QUEUE = "order.rejected";

    public static final String ORDER_SENT_FOR_APPROVAL_DLQ = "order.sent-for-approval.dlq";
    public static final String ORDER_APPROVED_DLQ = "order.approved.dlq";
    public static final String ORDER_REJECTED_DLQ = "order.rejected.dlq";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(EXCHANGE + ".dlq", true, false);
    }

    @Bean
    public Queue orderSentForApprovalQueue() {
        return QueueBuilder.durable(ORDER_SENT_FOR_APPROVAL_QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE + ".dlq")
                .withArgument("x-dead-letter-routing-key", ORDER_SENT_FOR_APPROVAL_QUEUE)
                .build();
    }

    @Bean
    public Queue orderApprovedQueue() {
        return QueueBuilder.durable(ORDER_APPROVED_QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE + ".dlq")
                .withArgument("x-dead-letter-routing-key", ORDER_APPROVED_QUEUE)
                .build();
    }

    @Bean
    public Queue orderRejectedQueue() {
        return QueueBuilder.durable(ORDER_REJECTED_QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE + ".dlq")
                .withArgument("x-dead-letter-routing-key", ORDER_REJECTED_QUEUE)
                .build();
    }

    @Bean
    public Queue orderSentForApprovalDlq() {
        return QueueBuilder.durable(ORDER_SENT_FOR_APPROVAL_DLQ).build();
    }

    @Bean
    public Queue orderApprovedDlq() {
        return QueueBuilder.durable(ORDER_APPROVED_DLQ).build();
    }

    @Bean
    public Queue orderRejectedDlq() {
        return QueueBuilder.durable(ORDER_REJECTED_DLQ).build();
    }

    @Bean
    public Binding orderSentForApprovalBinding(Queue orderSentForApprovalQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderSentForApprovalQueue).to(exchange).with(ORDER_SENT_FOR_APPROVAL_QUEUE);
    }

    @Bean
    public Binding orderApprovedBinding(Queue orderApprovedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderApprovedQueue).to(exchange).with(ORDER_APPROVED_QUEUE);
    }

    @Bean
    public Binding orderRejectedBinding(Queue orderRejectedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderRejectedQueue).to(exchange).with(ORDER_REJECTED_QUEUE);
    }

    @Bean
    public Binding orderSentForApprovalDlqBinding(Queue orderSentForApprovalDlq, TopicExchange dlqExchange) {
        return BindingBuilder.bind(orderSentForApprovalDlq).to(dlqExchange).with(ORDER_SENT_FOR_APPROVAL_QUEUE);
    }

    @Bean
    public Binding orderApprovedDlqBinding(Queue orderApprovedDlq, TopicExchange dlqExchange) {
        return BindingBuilder.bind(orderApprovedDlq).to(dlqExchange).with(ORDER_APPROVED_QUEUE);
    }

    @Bean
    public Binding orderRejectedDlqBinding(Queue orderRejectedDlq, TopicExchange dlqExchange) {
        return BindingBuilder.bind(orderRejectedDlq).to(dlqExchange).with(ORDER_REJECTED_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
