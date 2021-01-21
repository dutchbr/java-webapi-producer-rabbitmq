package com.integracao.producers.service;

import com.integracao.producers.dto.Message;

public interface AmqpService {
    void sendToConsumer(Message message);

}
