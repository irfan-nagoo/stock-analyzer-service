package com.example.dataproducer.exception;

import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.dataproducer.constants.MessagingConstants.PROCESSING_ERROR_MSG;
import static com.example.dataproducer.constants.MessagingConstants.VALIDATION_ERROR_MSG;

/**
 * @author irfan.nagoo
 */

@ControllerAdvice
@Slf4j
public class DataProducerControllerAdvice {

    @ExceptionHandler(CSVReadException.class)
    public ResponseEntity<ErrorResponse> handleCSVReadException(CSVReadException e) {
        log.error("Exception occurred while processing request: ", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Exception occurred while processing request: ", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException me) {
        log.error("Exception occurred while processing request: ", me);
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), VALIDATION_ERROR_MSG);
        me.getBindingResult().getFieldErrors()
                .forEach(e -> response.getErrors().add(new ErrorResponse.ValidationError(e.getField(), e.getDefaultMessage())));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<BaseResponse> handleFeignException(FeignException e) {
        log.error("Exception occurred while processing request: ", e);
        return ResponseEntity.status(e.status()).body(buildResponse(e.status(), e.contentUTF8()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception occurred while processing request: ", e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }

    private static BaseResponse buildResponse(int status, String errorMsg) {
        try {
            return new ObjectMapper().readValue(errorMsg, BaseResponse.class);
        } catch (JsonProcessingException ignore) {
            // ignore
        }
        return new BaseResponse(HttpStatus.valueOf(status).name(), PROCESSING_ERROR_MSG);
    }
}
