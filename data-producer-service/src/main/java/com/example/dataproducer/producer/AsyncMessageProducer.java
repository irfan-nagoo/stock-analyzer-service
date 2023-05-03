package com.example.dataproducer.producer;

/**
 * @author irfan.nagoo
 */
public interface AsyncMessageProducer<T> {

    void sentMessage(T object);
}
