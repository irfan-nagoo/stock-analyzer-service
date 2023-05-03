package com.example.dataproducer.exception;

import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.ErrorResponse;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Executable;
import java.util.Collections;
import java.util.Objects;

import static com.example.dataproducer.constants.MessagingConstants.PROCESSING_ERROR_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataProducerControllerAdviceTest {

    @InjectMocks
    private DataProducerControllerAdvice controllerAdvice;

    @Test
    void handleCSVReaderException() {
        CSVReadException exception = new CSVReadException("Parse Error", new Exception());
        ResponseEntity<ErrorResponse> response = controllerAdvice.handleCSVReadException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Parse Error", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Input argument Invalid");
        ResponseEntity<ErrorResponse> response = controllerAdvice.handleIllegalArgumentException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Input argument Invalid", response.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodParameter methodParameter = mock(MethodParameter.class);
        when(methodParameter.getExecutable()).thenReturn(mock(Executable.class));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getField()).thenReturn("name");
        when(fieldError.getDefaultMessage()).thenReturn("Invalid");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        ResponseEntity<ErrorResponse> response = controllerAdvice.handleMethodArgumentNotValidException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("name", Objects.requireNonNull(response.getBody()).getErrors().get(0).getField());
    }

    @Test
    void handleFeignException() {
        FeignException.FeignClientException.BadRequest exception = mock(FeignException.FeignClientException
                .BadRequest.class);
        when(exception.contentUTF8()).thenReturn("Some Content");
        when(exception.status()).thenReturn(HttpStatus.BAD_REQUEST.value());
        ResponseEntity<BaseResponse> response = controllerAdvice.handleFeignException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(PROCESSING_ERROR_MSG, Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void handleException() {
        Exception exception = new Exception(PROCESSING_ERROR_MSG);
        ResponseEntity<ErrorResponse> response = controllerAdvice.handleException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(PROCESSING_ERROR_MSG, response.getBody().getMessage());
    }
}