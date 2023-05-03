package com.example.dataconsumer.entity;

import com.example.dataconsumer.domain.StockHistoryObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author irfan.nagoo
 */

@Document("stockhistories")
@Getter
@Setter
public class StockHistory {

    @Id
    private String id;
    private String name;
    private LocalDate date;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long volume;
    private LocalDateTime createTimestamp;

    public StockHistoryObject toObject() {
        StockHistoryObject stockHistory = new StockHistoryObject();
        stockHistory.setId(this.id);
        stockHistory.setName(this.name);
        stockHistory.setDate(this.date);
        stockHistory.setOpenPrice(this.openPrice);
        stockHistory.setHighPrice(this.highPrice);
        stockHistory.setLowPrice(this.lowPrice);
        stockHistory.setClosePrice(this.closePrice);
        stockHistory.setVolume(this.volume);
        stockHistory.setCreateTimestamp(this.createTimestamp);
        return stockHistory;
    }
}
