package com.example.dataproducer.exception;

/**
 * @author irfan.nagoo
 */
public class CSVReadException extends RuntimeException {

    public CSVReadException(String message, Throwable e) {
        super(message, e);
    }
}
