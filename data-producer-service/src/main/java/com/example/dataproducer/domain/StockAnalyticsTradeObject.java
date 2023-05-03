package com.example.dataproducer.domain;

import com.example.dataproducer.constants.TradeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
public class StockAnalyticsTradeObject {

    private Long maxBuyVolume;
    private LocalDate maxBuyVolumeDate;
    private Long maxSellVolume;
    private LocalDate maxSellVolumeDate;
    private String avgBuyPercent;
    private String avgSellPercent;
    private TradeType majorityTradeAction;
}
