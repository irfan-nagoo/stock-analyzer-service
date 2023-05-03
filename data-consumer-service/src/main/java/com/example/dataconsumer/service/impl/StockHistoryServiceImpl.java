package com.example.dataconsumer.service.impl;

import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.entity.StockHistory;
import com.example.dataconsumer.exception.RecordNotFoundException;
import com.example.dataconsumer.repository.StockHistoryRepository;
import com.example.dataconsumer.response.BaseResponse;
import com.example.dataconsumer.response.StockHistoryResponse;
import com.example.dataconsumer.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.dataconsumer.constants.MessagingConstants.*;

/**
 * @author irfan.nagoo
 */


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StockHistoryServiceImpl implements StockHistoryService {

    private final StockHistoryRepository stockHistoryRepository;

    @Override
    public StockHistoryResponse getStockHistoryByTicker(String ticker, int pageNo, int pageSize
            , String sortBy, String sortDirection) {
        log.info("Getting records for Ticker [{}]", ticker);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, getOrderBy(sortBy, sortDirection));
        List<StockHistory> stockHistories = stockHistoryRepository.findByName(ticker, pageRequest);
        return buildStockHistoryResponse(ticker, stockHistories);
    }

    @Override
    public StockHistoryResponse getStockHistoryByDateRange(String ticker, LocalDate startDate,
                                                           LocalDate endDate, String sortBy, String sortDirection) {
        log.info("Getting records for Ticker [{}] with DateRange", ticker);
        Sort defaultSort = getOrderBy(sortBy, sortDirection);
        List<StockHistory> stockHistories = stockHistoryRepository.findByNameAndDateBetween(ticker, startDate, endDate, defaultSort);
        return buildStockHistoryResponse(ticker, stockHistories);
    }

    @Override
    public BaseResponse deleteByNameAndBDateRange(String ticker, LocalDate startDate, LocalDate endDate) {
        log.info("Deleting record for Ticker [{}]", ticker);
        stockHistoryRepository.deleteByNameAndDateBetween(ticker, startDate, endDate);
        log.info("Records deleted Successfully!");
        return new BaseResponse(HttpStatus.OK.name(), RECORDS_DELETED_MSG);
    }


    @Override
    public void processMessage(StockHistoryObject stockHistory) {
        log.info("Processing event with name [{}]", stockHistory.getName());
        validate(stockHistory);
        stockHistoryRepository.save(stockHistory.toEntity());
        log.info("Event saved successfully!");
    }

    private static void validate(StockHistoryObject stockHistory) {
        Objects.requireNonNull(stockHistory.getName(), "name");
        Objects.requireNonNull(stockHistory.getDate(), "date");
    }

    private static Sort getOrderBy(String sortBy, String sortDirection) {
        String[] sortByArr = sortBy.split(",");
        String[] sortDirectionArr = sortDirection.split(",");
        Sort sort = Sort.by(Sort.Direction.valueOf(sortDirectionArr[0].toUpperCase()), sortByArr[0]);
        for (int i = 1; i < sortByArr.length; i++) {
            sort = sort.and(Sort.by(Sort.Direction.valueOf(sortDirectionArr[i].toUpperCase()), sortByArr[i]));
        }
        return sort;
    }

    private static StockHistoryResponse buildStockHistoryResponse(String ticker, Collection<StockHistory> stockHistories) {
        if (!stockHistories.isEmpty()) {
            StockHistoryResponse response = new StockHistoryResponse(HttpStatus.OK.name(),
                    String.format(TOTAL_RECORD_MSG, stockHistories.size()));
            response.setStockHistory(stockHistories.stream()
                    .map(StockHistory::toObject).collect(Collectors.toList()));
            return response;
        } else {
            throw new RecordNotFoundException(String.format(NOT_FOUND_MSG, ticker));
        }
    }

}
