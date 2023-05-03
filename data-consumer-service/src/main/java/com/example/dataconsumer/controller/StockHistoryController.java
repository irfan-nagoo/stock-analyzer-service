package com.example.dataconsumer.controller;

import com.example.dataconsumer.response.BaseResponse;
import com.example.dataconsumer.response.StockHistoryResponse;
import com.example.dataconsumer.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock-history")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    /**
     * Returns the Paginated and Sorted Stock history of a given Stock
     *
     * @param ticker        Stock Symbol
     * @param pageNo        Page Number
     * @param pageSize      Page Size
     * @param sortBy        Comma delimited list of Sort field
     * @param sortDirection Comma delimited list of Sort direction
     * @return Stock history
     */
    @GetMapping("/ticker/{ticker}/list")
    public StockHistoryResponse getStockHistoryByTicker(@PathVariable("ticker") String ticker, @RequestParam("pageNo") int pageNo,
                                                        @RequestParam("pageSize") int pageSize, @RequestParam("sortBy") String sortBy,
                                                        @RequestParam("sortDirection") String sortDirection) {
        return stockHistoryService.getStockHistoryByTicker(ticker, pageNo, pageSize, sortBy, sortDirection);
    }

    /**
     * Return the stock history for given Stock in a given date range
     *
     * @param ticker        Stock symbol
     * @param startDate     Period Start date
     * @param endDate       Period End date
     * @param sortBy        Comma delimited list of Sort field
     * @param sortDirection Comma delimited list of Sort direction
     * @return Stock history
     */
    @GetMapping("/ticker/{ticker}/date")
    public StockHistoryResponse getStockHistoryByDateRange(@PathVariable("ticker") String ticker,
                                                           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                           @RequestParam("sortBy") String sortBy,
                                                           @RequestParam("sortDirection") String sortDirection) {
        return stockHistoryService.getStockHistoryByDateRange(ticker, startDate, endDate, sortBy, sortDirection);
    }

    /**
     * Delete records by given stock symbol and date range
     *
     * @param ticker    Stock Symbol
     * @param startDate Start date
     * @param endDate   End date
     * @return Response
     */
    @DeleteMapping("/ticker/{ticker}/delete")
    public BaseResponse deleteByNameAndBDateRange(@PathVariable("ticker") String ticker,
                                                  @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return stockHistoryService.deleteByNameAndBDateRange(ticker, startDate, endDate);
    }
}
