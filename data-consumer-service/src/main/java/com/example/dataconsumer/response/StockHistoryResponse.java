package com.example.dataconsumer.response;

import com.example.dataconsumer.domain.StockHistoryObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
public class StockHistoryResponse extends BaseResponse {

    private List<StockHistoryObject> stockHistory;

    public StockHistoryResponse(String code, String message) {
        super(code, message);
    }
}
