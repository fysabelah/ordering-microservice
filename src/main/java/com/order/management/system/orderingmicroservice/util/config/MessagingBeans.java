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
    private String stockUpdateQueue;

    @Value("${messaging.queue.payment}")
    private String paymentProcessQueue;

    @Value("${messaging.queue.transportation.cancel}")
    private String transportSendCancelation;

    public MessagingBeans(CachingConnectionFactory cachingConnectionFactory) {
        this.cachingConnectionFactory = cachingConnectionFactory;
    }

    @Bean
    public Queue createClientValidateQueue() {
        return QueueBuilder.durable(clientValidateQueue)
                .withArgument("x-dead-letter-exchange", "x.process-failure")
                .withArgument("x-dead-letter-routing-key", "client")
                .build();
    }

    @Bean
    public Queue createStatusUpdateQueue() {
        return new Queue(statusUpdateQueue, true);
    }

    @Bean
    public Queue createStockProcessQueue() {
        return new Queue(stockProcessQueue, true);
    }

    @Bean
    public Queue createStockCancelationQueue() {
        return new Queue(stockUpdateQueue, true);
    }

    @Bean
    public Queue createPaymentProcessQueue() {
        return new Queue(paymentProcessQueue, true);
    }

    @Bean
    public Queue createTransportationCancelQueue() {
        return new Queue(transportSendCancelation, true);
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
                new DirectExchange("x.process-failure"),
                new Queue("q.fall-back-process"),
                new Binding("q.fall-back-process", Binding.DestinationType.QUEUE, "x.process-failure", "client", null)
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
