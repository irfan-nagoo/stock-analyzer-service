package com.example.dataconsumer.service;

import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.response.BaseResponse;
import com.example.dataconsumer.response.StockHistoryResponse;

import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */
public interface StockHistoryService {

    StockHistoryResponse getStockHistoryByTicker(String ticker, int pageNo, int pageSize
            , String sortBy, String sortDirection);

    StockHistoryResponse getStockHistoryByDateRange(String ticker, LocalDate startDate, LocalDate endDate,
                                                    String sortBy, String sortDirection);

    BaseResponse deleteByNameAndBDateRange(String ticker, LocalDate startDate, LocalDate endDate);

    void processMessage(StockHistoryObject stockHistory);
}
