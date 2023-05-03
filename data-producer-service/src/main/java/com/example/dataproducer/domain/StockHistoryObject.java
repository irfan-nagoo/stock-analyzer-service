package com.example.dataproducer.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotEmpty
    private String name;
    @NotNull
    private LocalDate date;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    @NotNull
    private Long volume;
    private LocalDateTime createTimestamp;

}
