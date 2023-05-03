package com.example.dataproducer.response;

import com.example.dataproducer.domain.StockAnalyticsObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */

@Setter
@Getter
@NoArgsConstructor
public class StockAnalyticsResponse extends BaseResponse {

    public StockAnalyticsResponse(String code, String message) {
        super(code, message);
    }

    private String stockName;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private StockAnalyticsObject stockAnalytics;
}
