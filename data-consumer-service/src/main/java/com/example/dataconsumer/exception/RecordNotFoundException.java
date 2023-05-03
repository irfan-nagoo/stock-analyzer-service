package com.example.dataconsumer.exception;

/**
 * @author irfan.nagoo
 */
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
