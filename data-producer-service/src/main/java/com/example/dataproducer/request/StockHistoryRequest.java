package com.example.dataproducer.request;

import com.example.dataproducer.domain.StockHistoryObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
public class StockHistoryRequest {

    @Valid
    private StockHistoryObject stockHistory;
}
