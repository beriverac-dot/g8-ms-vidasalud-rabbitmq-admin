package com.vidasalud.ms_vidasalud_rabbitmq_admin.service;

import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    public void createQueueAndBinding(String queueName, String exchangeName, String routingKey) {
        // Declarar infraestructura DLQ
        DirectExchange dlqExchange = new DirectExchange("dlq.exchange");
        Queue dlqQueue = QueueBuilder.durable(queueName + ".dlq").build();
        Binding dlqBinding = BindingBuilder.bind(dlqQueue).to(dlqExchange).with(queueName + ".routing.dlq");
        
        amqpAdmin.declareExchange(dlqExchange);
        amqpAdmin.declareQueue(dlqQueue);
        amqpAdmin.declareBinding(dlqBinding);

        // Declarar la cola principal vinculada a la DLQ
        Queue mainQueue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "dlq.exchange")
                .withArgument("x-dead-letter-routing-key", queueName + ".routing.dlq")
                .build();
                
        DirectExchange mainExchange = new DirectExchange(exchangeName);
        Binding mainBinding = BindingBuilder.bind(mainQueue).to(mainExchange).with(routingKey);

        amqpAdmin.declareQueue(mainQueue);
        amqpAdmin.declareExchange(mainExchange);
        amqpAdmin.declareBinding(mainBinding);
    }

    public void deleteQueue(String queueName) {
        amqpAdmin.deleteQueue(queueName);
        amqpAdmin.deleteQueue(queueName + ".dlq");
    }
}