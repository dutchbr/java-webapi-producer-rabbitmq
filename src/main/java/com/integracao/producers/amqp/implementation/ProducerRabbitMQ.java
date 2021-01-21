package com.integracao.producers.amqp.implementation;

import com.integracao.producers.amqp.AmqpProducer;
import com.integracao.producers.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.*;

@Component
public class ProducerRabbitMQ implements AmqpProducer<Message> {
    private static final Logger LOG = LoggerFactory
            .getLogger(ProducerRabbitMQ.class);



    @Value("${spring.rabbitmq.request.exchange.producer}")
    private String exchangeName;
    @Value("${spring.rabbitmq.request.deadletter.producer}")
    private String deadLetter;
    @Value("${spring.rabbitmq.request.exchange.producer}")
    private String exchange;
    private final List<String> queueList;

    @Bean
    DirectExchange exchange()
    {
        return new DirectExchange(exchange);
    }
    @Bean
    Queue deadLetter()
    {
        return new Queue(deadLetter);
    }

    @Bean
    public Binding bindingDeadLetter() {

        return BindingBuilder.bind(deadLetter()).to(exchange()).with(deadLetter);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin admin;


    public ProducerRabbitMQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        queueList = Collections.synchronizedList(new ArrayList<>());

    }

    private void CheckQueue(String queueName) {
        if (!queueList.contains(queueName)) {
            CreateQueue(queueName);

        }
    }

    private synchronized void CreateQueue(String queueName) {
        try {
            Map<String,Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange",exchangeName);
            args.put("x-dead-letter-routing-key",deadLetter);
            Queue queue = new Queue(queueName, true, false, false,args);
            Binding binding = new Binding(queueName,Binding.DestinationType.QUEUE, exchangeName, queueName, args);
            admin.declareQueue(queue);
            admin.declareBinding(binding);
            queueList.add(queueName);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            System.out.println(e.getMessage());

        }

    }


    @Override
    public void producer(Message message) {
        try {
            CheckQueue(message.getFila());
            rabbitTemplate.convertAndSend(exchangeName,message.getFila(), message.getJson());

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new AmqpRejectAndDontRequeueException(ex);

        }


    }
}


