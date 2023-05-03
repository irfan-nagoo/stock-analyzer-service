package com.example.dataproducer.service.impl;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.feign.StockHistoryFeignClient;
import com.example.dataproducer.producer.AsyncMessageProducer;
import com.example.dataproducer.reader.StockHistoryReader;
import com.example.dataproducer.request.StockHistoryRequest;
import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import com.example.dataproducer.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.example.dataproducer.constants.MessagingConstants.CSV_FILE_REQUIRED_ERROR_MSG;
import static com.example.dataproducer.constants.MessagingConstants.REQUEST_PROCESSED_MSG;

/**
 * @author irfan.nagoo
 */


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StockHistoryServiceImpl implements StockHistoryService {

    private final AsyncMessageProducer<StockHistoryObject> messageProducer;
    private final StockHistoryFeignClient feignClient;

    @Override
    public StockHistoryResponse getStockHistoryByTicker(String ticker, int pageNo, int pageSize
            , String sortBy, String sortDirection) {
        log.info("Getting records for ticker [{}]", ticker);
        return feignClient.getStockHistoryByName(ticker, pageNo, pageSize
                , sortBy, sortDirection);
    }

    @Override
    public BaseResponse processStockHistory(StockHistoryRequest request) {
        log.info("Processing request for stock [{}]", request.getStockHistory().getName());
        messageProducer.sentMessage(request.getStockHistory());
        return new BaseResponse(HttpStatus.OK.name(), String.format(REQUEST_PROCESSED_MSG,
                request.getStockHistory().getName()));
    }

    @Override
    public BaseResponse processBulkStockHistory(String ticker, List<MultipartFile> files) {
        log.info("Processing bulk request for stock [{}]", ticker);
        if (!CollectionUtils.isEmpty(files)) {
            files.stream()
                    .map(csvFile -> new StockHistoryReader(ticker, csvFile).parse())
                    .flatMap(Collection::stream)
                    .forEach(messageProducer::sentMessage);
        } else {
            throw new IllegalArgumentException(CSV_FILE_REQUIRED_ERROR_MSG);
        }
        return new BaseResponse(HttpStatus.OK.name(), String.format(REQUEST_PROCESSED_MSG,
                ticker));
    }

    @Override
    public BaseResponse deleteByNameAndBDateRange(String ticker, LocalDate startDate, LocalDate endDate) {
        log.info("Delete request for stock [{}]", ticker);
        return feignClient.deleteByNameAndBDateRange(ticker, startDate, endDate);
    }

}
