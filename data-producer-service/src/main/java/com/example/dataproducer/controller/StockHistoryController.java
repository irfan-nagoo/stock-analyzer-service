package com.example.dataproducer.controller;

import com.example.dataproducer.request.StockHistoryRequest;
import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import com.example.dataproducer.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * @author irfan.nagoo
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock-history")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    /**
     * Save single Stock history record
     *
     * @param ticker  Stock symbol
     * @param request Single Stock history record
     * @return Response with status and message
     */
    @PostMapping("/ticker/{ticker}/save")
    public BaseResponse processStockHistory(@PathVariable("ticker") String ticker,
                                            @Valid @RequestBody StockHistoryRequest request) {
        return stockHistoryService.processStockHistory(request);
    }

    /**
     * Save bulk history of a Stock by reading data from list of input CSV files
     *
     * @param ticker Stock symbol
     * @param files  List of csv files with stock history records
     * @return Response with status and message
     */
    @PostMapping(value = "/ticker/{ticker}/bulk-save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse processBulkStockHistory(@PathVariable("ticker") String ticker
            , @RequestParam("files") List<MultipartFile> files) {
        return stockHistoryService.processBulkStockHistory(ticker, files);
    }

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
        return stockHistoryService.getStockHistoryByTicker(ticker, pageNo, pageSize
                , sortBy, sortDirection);
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
