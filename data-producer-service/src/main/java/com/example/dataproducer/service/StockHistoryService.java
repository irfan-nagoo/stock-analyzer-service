package com.example.dataproducer.service;

import com.example.dataproducer.request.StockHistoryRequest;
import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * @author irfan.nagoo
 */
public interface StockHistoryService {

    StockHistoryResponse getStockHistoryByTicker(String ticker, int pageNo, int pageSize
            , String sortBy, String sortDirection);

    BaseResponse processStockHistory(StockHistoryRequest request);

    BaseResponse processBulkStockHistory(String ticker, List<MultipartFile> files);

    BaseResponse deleteByNameAndBDateRange(String ticker, LocalDate startDate, LocalDate endDate);
}
