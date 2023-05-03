package com.example.dataconsumer.consumer;

/**
 * @author irfan.nagoo
 */
public interface AsyncMessageConsumer<T> {

    void receiveMessage(T object);
}
