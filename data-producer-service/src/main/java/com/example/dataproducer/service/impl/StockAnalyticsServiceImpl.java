package com.example.dataproducer.service.impl;

import com.example.dataproducer.constants.StockPerformanceType;
import com.example.dataproducer.constants.TradeType;
import com.example.dataproducer.domain.StockAnalyticsObject;
import com.example.dataproducer.domain.StockAnalyticsTradeObject;
import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.feign.StockHistoryFeignClient;
import com.example.dataproducer.response.StockAnalyticsResponse;
import com.example.dataproducer.response.StockAnalyticsTradeResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import com.example.dataproducer.service.StockAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.example.dataproducer.constants.DataProducerConstants.*;
import static com.example.dataproducer.constants.MessagingConstants.*;

/**
 * @author irfan.nagoo
 */

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StockAnalyticsServiceImpl implements StockAnalyticsService {

    private final StockHistoryFeignClient stockHistoryFeignClient;

    @Override
    public StockAnalyticsResponse getStockAnalytics(@NonNull String ticker, @NonNull LocalDate startDate,
                                                    @NonNull LocalDate endDate) {
        log.info("Start Analytics for Ticker [{}]", ticker);
        validateDate(startDate, endDate);
        StockHistoryResponse stockHistoryResponse = stockHistoryFeignClient.getStockHistoryByDateRange(ticker, startDate, endDate,
                DATE_SORT_FIELD, ASC_SORT_DIRECTION);
        List<StockHistoryObject> stockHistoryList = stockHistoryResponse.getStockHistory();
        int totalSamples = stockHistoryList.size();

        StockAnalyticsObject stockAnalytics = new StockAnalyticsObject();

        // periodOpenPrice is first opening price
        stockAnalytics.setPeriodOpenPrice(applyRounding(stockHistoryList.get(0).getOpenPrice()));

        // periodClosePrice is last closing price
        stockAnalytics.setPeriodClosePrice(applyRounding(stockHistoryList.get(totalSamples - 1).getClosePrice()));

        // periodLowPrice is the smallest lowPrice
        StockHistoryObject minStockHistory = stockHistoryList.stream()
                .min(Comparator.comparing(StockHistoryObject::getLowPrice)).get();
        stockAnalytics.setPeriodLowPrice(applyRounding(minStockHistory.getLowPrice()));
        stockAnalytics.setPeriodLowPriceDate(minStockHistory.getDate());

        // periodHighPrice is the largest highPrice
        StockHistoryObject maxStockHistory = stockHistoryList.stream()
                .max(Comparator.comparing(StockHistoryObject::getHighPrice)).get();
        stockAnalytics.setPeriodHighPrice(applyRounding(maxStockHistory.getHighPrice()));
        stockAnalytics.setPeriodHighPriceDate(maxStockHistory.getDate());

        // averagePrice is the average of all closePrice
        BigDecimal avgPrice = stockHistoryList.stream()
                .map(StockHistoryObject::getClosePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(totalSamples), 4, RoundingMode.HALF_UP);
        stockAnalytics.setPeriodAveragePrice(applyRounding(avgPrice));

        // stockPerformance && pricePercentChange is based on period first openPrice and last closePrice
        populateStockPerformanceAndPercentChange(stockAnalytics, stockHistoryList);

        // initialMarketCap is based on first openPrice and volume
        BigDecimal initialMarketCap = stockHistoryList.get(0).getOpenPrice()
                .multiply(BigDecimal.valueOf(stockHistoryList.get(0).getVolume()));
        stockAnalytics.setInitialMarketCap(applyRounding(initialMarketCap));

        // latestMarketCap is based on latest closePrice and volume
        BigDecimal latestMarketCap = stockHistoryList.get(totalSamples - 1).getClosePrice()
                .multiply(BigDecimal.valueOf(stockHistoryList.get(totalSamples - 1).getVolume()));
        stockAnalytics.setLatestMarketCap(applyRounding(latestMarketCap));

        // build response
        StockAnalyticsResponse stockAnalyticsResponse = new StockAnalyticsResponse(HttpStatus.OK.name(),
                String.format(REQUEST_PROCESSED_MSG, ticker));
        stockAnalyticsResponse.setStockName(ticker);
        stockAnalyticsResponse.setPeriodStartDate(startDate);
        stockAnalyticsResponse.setPeriodEndDate(endDate);
        stockAnalyticsResponse.setStockAnalytics(stockAnalytics);
        log.info("End Analytics for Ticker [{}]", ticker);
        return stockAnalyticsResponse;
    }

    @Override
    public StockAnalyticsTradeResponse getStockAnalyticsTrade(String ticker, int month) {
        log.info("Start Analytic Trade for Ticker [{}]", ticker);
        validateMonth(month);
        StockHistoryResponse stockHistoryResponse = stockHistoryFeignClient.getStockHistoryByName(ticker, 0, Integer.MAX_VALUE,
                DATE_SORT_FIELD, ASC_SORT_DIRECTION);

        List<StockHistoryObject> stockHistoryList = stockHistoryResponse.getStockHistory();
        // filter records for given month only
        stockHistoryList = stockHistoryList.stream()
                .filter(sh -> sh.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
        int totalSamples = stockHistoryList.size();

        StockAnalyticsTradeObject stockAnalyticsTrade = new StockAnalyticsTradeObject();

        // calculate days on which the volume of the stock raise up in a given month
        // max stocks sold and max stocks bought
        int i = -1;
        int stockVolumeUpDaysCount = 0;
        int samplesSkipped = 0;
        long maxBuyVolume = 0L;
        long maxSellVolume = 0L;
        LocalDate maxBuyDate = null;
        LocalDate maxSellDate = null;
        for (StockHistoryObject currStockHistory : stockHistoryList) {
            if (i == -1) {
                i++;
                samplesSkipped++;
                continue;
            }
            StockHistoryObject prevStockHistory = stockHistoryList.get(i);
            // start again for each new year
            if (currStockHistory.getDate().getYear() > prevStockHistory.getDate().getYear()) {
                i++;
                samplesSkipped++;
                continue;
            }

            long volDiff = currStockHistory.getVolume() - prevStockHistory.getVolume();
            if (volDiff < 0) {
                // Buy
                if (maxBuyVolume < Math.abs(volDiff)) {
                    maxBuyVolume = Math.abs(volDiff);
                    maxBuyDate = currStockHistory.getDate();
                }
            } else {
                // Sell
                if (maxSellVolume < volDiff) {
                    maxSellVolume = volDiff;
                    maxSellDate = currStockHistory.getDate();
                }
            }
            stockVolumeUpDaysCount += compareVolume(currStockHistory.getVolume(), prevStockHistory.getVolume());
            i++;
        }

        // calculate average sell/buy percent in a given month
        int actualSamples = totalSamples - samplesSkipped;
        double sellPercent = ((double) stockVolumeUpDaysCount / actualSamples) * 100;
        double buyPercent = ((double) (actualSamples - stockVolumeUpDaysCount) / actualSamples) * 100;

        // sett the required fields
        stockAnalyticsTrade.setAvgBuyPercent(String.format("%.02f", buyPercent) + PERCENT);
        stockAnalyticsTrade.setAvgSellPercent(String.format("%.02f", sellPercent) + PERCENT);
        stockAnalyticsTrade.setMaxBuyVolume(maxBuyVolume);
        stockAnalyticsTrade.setMaxBuyVolumeDate(maxBuyDate);
        stockAnalyticsTrade.setMaxSellVolume(maxSellVolume);
        stockAnalyticsTrade.setMaxSellVolumeDate(maxSellDate);
        stockAnalyticsTrade.setMajorityTradeAction(buyPercent >= sellPercent ? TradeType.BUY : TradeType.SELL);

        // build response
        StockAnalyticsTradeResponse stockAnalyticsSuggestionResponse = new StockAnalyticsTradeResponse(HttpStatus.OK.name(),
                String.format(REQUEST_PROCESSED_MSG, ticker));
        stockAnalyticsSuggestionResponse.setStockName(ticker);
        stockAnalyticsSuggestionResponse.setMonth(LocalDate.of(0, month, 1)
                .getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        stockAnalyticsSuggestionResponse.setDuration(getDuration(stockHistoryList));
        stockAnalyticsSuggestionResponse.setStockAnalyticsTrade(stockAnalyticsTrade);
        log.info("End Analytic Trade for Ticker [{}]", ticker);
        return stockAnalyticsSuggestionResponse;
    }

    private static void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(INVALID_MONTH_ERROR_MSG);
        }
    }

    private static void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(INVALID_DATE_RANGE_ERROR_MSG);
        }
    }

    private static int compareVolume(long v1, long v2) {
        return v1 >= v2 ? 1 : 0;
    }

    private static String getDuration(List<StockHistoryObject> stockHistoryList) {
        return stockHistoryList.get(0).getDate().getYear() + "-" +
                stockHistoryList.get(stockHistoryList.size() - 1).getDate().getYear();
    }

    private static void populateStockPerformanceAndPercentChange(StockAnalyticsObject stockAnalytics,
                                                                 List<StockHistoryObject> stockHistoryList) {
        BigDecimal firstOpenPrice = stockHistoryList.get(0).getOpenPrice();
        BigDecimal lastClosePrice = stockHistoryList.get(stockHistoryList.size() - 1).getClosePrice();
        BigDecimal pricePercentChange = lastClosePrice
                .subtract(firstOpenPrice)
                .divide(firstOpenPrice, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        if (isBetween(pricePercentChange, BigDecimal.valueOf(Long.MIN_VALUE), BigDecimal.ZERO)) {
            stockAnalytics.setStockPerformance(StockPerformanceType.BAD);
            stockAnalytics.setPricePercentChange(pricePercentChange + PERCENT);
        } else if (isBetween(pricePercentChange, BigDecimal.ZERO, BigDecimal.valueOf(30))) {
            stockAnalytics.setStockPerformance(StockPerformanceType.AVERAGE);
            stockAnalytics.setPricePercentChange(pricePercentChange + PERCENT);
        } else if (isBetween(pricePercentChange, BigDecimal.valueOf(30), BigDecimal.valueOf(60))) {
            stockAnalytics.setStockPerformance(StockPerformanceType.GOOD);
            stockAnalytics.setPricePercentChange(pricePercentChange + PERCENT);
        } else if (isBetween(pricePercentChange, BigDecimal.valueOf(60), BigDecimal.valueOf(100))) {
            stockAnalytics.setStockPerformance(StockPerformanceType.VERY_GOOD);
            stockAnalytics.setPricePercentChange(pricePercentChange + PERCENT);
        } else if (isBetween(pricePercentChange, BigDecimal.valueOf(100), BigDecimal.valueOf(Long.MAX_VALUE))) {
            stockAnalytics.setStockPerformance(StockPerformanceType.EXCELLENT);
            stockAnalytics.setPricePercentChange(pricePercentChange + PERCENT);
        }
    }

    private static boolean isBetween(BigDecimal num, BigDecimal val1, BigDecimal val2) {
        return num.compareTo(val2) == 0 || (num.compareTo(val1) > 0 && num.compareTo(val2) < 0);
    }

    private static BigDecimal applyRounding(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_UP);
    }
}
