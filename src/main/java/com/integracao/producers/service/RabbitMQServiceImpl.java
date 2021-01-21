package com.integracao.producers.service;

import com.integracao.producers.amqp.AmqpProducer;
import com.integracao.producers.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements  AmqpService {

    @Autowired
    private AmqpProducer<Message>  amqpProducer;


    @Override
    public void sendToConsumer(Message message) {
        amqpProducer.producer(message);

    }
}
