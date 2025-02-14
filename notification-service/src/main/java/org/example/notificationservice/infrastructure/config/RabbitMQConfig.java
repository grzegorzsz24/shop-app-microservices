package org.example.notificationservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import org.example.notificationservice.infrastructure.exception.InvalidPayloadFatalExceptionStrategy;

@Configuration
@RequiredArgsConstructor
class RabbitMQConfig {
    private final LocalValidatorFactoryBean validatorFactoryBean;

    @Value("${app.rabbitmq.queues.mail}")
    private String mailQueue;

    @Value("${app.rabbitmq.exchanges.mail}")
    private String mailExchange;

    @Value("${app.rabbitmq.queues.mail-dead-letter}")
    private String mailDeadLetterQueue;

    @Value("${app.rabbitmq.exchanges.mail-dlx-exchange}")
    private String mailDlxExchange;

    @Value("${app.rabbitmq.routing-keys.mail}")
    private String routingKey;

    @Bean
    Queue messagesQueue() {
        return QueueBuilder.durable(mailQueue)
                .withArgument("x-dead-letter-exchange", mailDlxExchange)
                .withArgument("x-max-priority", 3)
                .build();
    }

    @Bean
    DirectExchange messagesExchange() {
        return new DirectExchange(mailExchange);
    }

    @Bean
    Binding bindingMessages() {
        return BindingBuilder.bind(messagesQueue())
                .to(messagesExchange()).with(routingKey);
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(mailDeadLetterQueue).build();
    }

    @Bean
    FanoutExchange deadLetterExchange() {
        return new FanoutExchange(mailDlxExchange);
    }

    @Bean
    Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange());
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
        rabbitListenerEndpointRegistrar.setValidator(this.validatorFactoryBean);
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerContainerFactory =
                new SimpleRabbitListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(connectionFactory);
        listenerContainerFactory.setErrorHandler(
                new ConditionalRejectingErrorHandler(
                        new InvalidPayloadFatalExceptionStrategy()));
        listenerContainerFactory.setMessageConverter(messageConverter());
        listenerContainerFactory.setDefaultRequeueRejected(false);
        return listenerContainerFactory;
    }
}
