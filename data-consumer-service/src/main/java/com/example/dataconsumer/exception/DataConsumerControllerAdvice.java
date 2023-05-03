package com.example.dataconsumer.exception;

import com.example.dataconsumer.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author irfan.nagoo
 */

@ControllerAdvice
@Slf4j
public class DataConsumerControllerAdvice {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RecordNotFoundException e) {
        log.error("Exception occurred while processing request: ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.name(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception occurred while processing request: ", e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }
}
