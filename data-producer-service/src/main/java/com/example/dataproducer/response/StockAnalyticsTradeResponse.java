package com.example.dataproducer.response;

import com.example.dataproducer.domain.StockAnalyticsTradeObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
@NoArgsConstructor
public class StockAnalyticsTradeResponse extends BaseResponse {

    public StockAnalyticsTradeResponse(String code, String message) {
        super(code, message);
    }

    private String stockName;
    private String month;
    private String duration;
    private StockAnalyticsTradeObject stockAnalyticsTrade;
}
