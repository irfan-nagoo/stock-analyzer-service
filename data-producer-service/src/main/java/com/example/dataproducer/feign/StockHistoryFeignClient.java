package com.example.dataproducer.feign;

import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */

@FeignClient(name = "stockHistoryFeignClient", url = "${com.example.data.producer.feign.stock.uri}")
public interface StockHistoryFeignClient {

    @GetMapping("/stock-history/ticker/{ticker}/list")
    StockHistoryResponse getStockHistoryByName(@PathVariable("ticker") String ticker, @RequestParam("pageNo") int pageNo,
                                               @RequestParam("pageSize") int pageSize, @RequestParam("sortBy") String sortBy,
                                               @RequestParam("sortDirection") String sortDirection);

    @GetMapping("/stock-history/ticker/{ticker}/date")
    StockHistoryResponse getStockHistoryByDateRange(@PathVariable("ticker") String ticker,
                                                    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                    @RequestParam("sortBy") String sortBy,
                                                    @RequestParam("sortDirection") String sortDirection);

    @DeleteMapping("/stock-history/ticker/{ticker}/delete")
    BaseResponse deleteByNameAndBDateRange(@PathVariable("ticker") String ticker,
                                           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
}
