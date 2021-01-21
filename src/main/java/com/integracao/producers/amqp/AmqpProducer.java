package com.integracao.producers.amqp;

public interface AmqpProducer<T> {
    void producer(T t);


}
