package com.example.dataconsumer.domain;

import com.example.dataconsumer.entity.StockHistory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
public class StockHistoryObject {

    private String id;
    private String name;
    private LocalDate date;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long volume;
    private LocalDateTime createTimestamp;

    public StockHistory toEntity() {
        StockHistory stockHistory = new StockHistory();
        stockHistory.setId(this.id);
        stockHistory.setName(this.name);
        stockHistory.setDate(this.date);
        stockHistory.setOpenPrice(this.openPrice);
        stockHistory.setHighPrice(this.highPrice);
        stockHistory.setLowPrice(this.lowPrice);
        stockHistory.setClosePrice(this.closePrice);
        stockHistory.setVolume(this.volume);
        stockHistory.setCreateTimestamp(LocalDateTime.now());
        return stockHistory;
    }
}
