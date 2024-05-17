package com.order.management.system.orderingmicroservice.util.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class MessagingBeans {

    private final CachingConnectionFactory cachingConnectionFactory;

    @Value("${messaging.queue.client}")
    private String clientValidateQueue;

    @Value("${messaging.queue.status}")
    private String statusUpdateQueue;

    @Value("${messaging.queue.stock}")
    private String stockProcessQueue;

    @Value("${messaging.queue.stock.cancelation}")
    private String stockCancelQueue;

    @Value("${messaging.queue.payment}")
    private String paymentProcessQueue;

    @Value("${messaging.queue.transportation.cancel}")
    private String transportSendCancelation;

    private static final String EXCHANGE_FALLBACK = "x.process-failure";

    private static final String QUEUE_FALLBACK = "q.fall-back-process";

    private static final String DEAD_LETTER_EXCHANGE_KEY = "x-dead-letter-exchange";

    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    public MessagingBeans(CachingConnectionFactory cachingConnectionFactory) {
        this.cachingConnectionFactory = cachingConnectionFactory;
    }

    @Bean
    public Queue createClientValidateQueue() {
        return QueueBuilder.durable(clientValidateQueue)
                .withArgument(DEAD_LETTER_EXCHANGE_KEY, EXCHANGE_FALLBACK)
                .withArgument(DEAD_LETTER_ROUTING_KEY, "client")
                .build();
    }

    @Bean
    public Queue createStatusUpdateQueue() {
        return new Queue(statusUpdateQueue, true);
    }

    @Bean
    public Queue createStockProcessQueue() {
        return QueueBuilder.durable(stockProcessQueue)
                .withArgument(DEAD_LETTER_EXCHANGE_KEY, EXCHANGE_FALLBACK)
                .withArgument(DEAD_LETTER_ROUTING_KEY, "stock-reservation")
                .build();
    }

    @Bean
    public Queue createStockCancelationQueue() {
        return QueueBuilder.durable(stockCancelQueue)
                .withArgument(DEAD_LETTER_EXCHANGE_KEY, EXCHANGE_FALLBACK)
                .withArgument(DEAD_LETTER_ROUTING_KEY, "stock-cancelation")
                .build();
    }

    @Bean
    public Queue createPaymentProcessQueue() {
        return QueueBuilder.durable(paymentProcessQueue)
                .withArgument(DEAD_LETTER_EXCHANGE_KEY, EXCHANGE_FALLBACK)
                .withArgument(DEAD_LETTER_ROUTING_KEY, "payment")
                .build();
    }

    @Bean
    public Queue createTransportationCancelQueue() {
        return QueueBuilder.durable(transportSendCancelation)
                .withArgument(DEAD_LETTER_EXCHANGE_KEY, EXCHANGE_FALLBACK)
                .withArgument(DEAD_LETTER_ROUTING_KEY, "logistics")
                .build();
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless().maxAttempts(3)
                .backOffOptions(2000, 2.0, 100000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        configurer.configure(factory, cachingConnectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setBatchSize(5);
        factory.setAdviceChain(retryInterceptor());

        return factory;
    }

    @Bean
    public Declarables createDeadLetterSchema() {
        return new Declarables(
                new DirectExchange(EXCHANGE_FALLBACK),
                new Queue(QUEUE_FALLBACK),
                new Binding(QUEUE_FALLBACK, Binding.DestinationType.QUEUE, EXCHANGE_FALLBACK, "client", null),
                new Binding(QUEUE_FALLBACK, Binding.DestinationType.QUEUE, EXCHANGE_FALLBACK, "stock-reservation", null),
                new Binding(QUEUE_FALLBACK, Binding.DestinationType.QUEUE, EXCHANGE_FALLBACK, "stock-cancelation", null),
                new Binding(QUEUE_FALLBACK, Binding.DestinationType.QUEUE, EXCHANGE_FALLBACK, "payment", null),
                new Binding(QUEUE_FALLBACK, Binding.DestinationType.QUEUE, EXCHANGE_FALLBACK, "logistics", null)
        );
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);

        return rabbitTemplate;
    }
}
