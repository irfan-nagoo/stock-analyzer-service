package com.example.dataproducer.constants;

/**
 * @author irfan.nagoo
 */
public interface MessagingConstants {

    String REQUEST_PROCESSED_MSG = "Request Successfully Processed for Ticker [%s]";
    String CSV_READ_ERROR_MSG = "Error while parsing CSV file";
    String CSV_FILE_REQUIRED_ERROR_MSG = "CSV File is required to process Bulk request";
    String VALIDATION_ERROR_MSG = "Validation error has occurred";
    String INVALID_DATE_RANGE_ERROR_MSG = "The given date range is invalid";
    String INVALID_MONTH_ERROR_MSG = "The given month is invalid";
    String PROCESSING_ERROR_MSG = "Error occurred while processing the request";
}
