package com.example.dataconsumer.exception;

import com.example.dataconsumer.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class DataConsumerControllerAdviceTest {

    @InjectMocks
    private DataConsumerControllerAdvice controllerAdvice;

    @Test
    void handleNotFoundException() {
        RecordNotFoundException exception = new RecordNotFoundException("Record Not Found");
        ResponseEntity<ErrorResponse> response = controllerAdvice.handleNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Record Not Found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void handleException() {
        Exception exception = new Exception("Processing Error has Occurred");
        ResponseEntity<ErrorResponse> response = controllerAdvice.handleException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Processing Error has Occurred", Objects.requireNonNull(response.getBody()).getMessage());
    }
}