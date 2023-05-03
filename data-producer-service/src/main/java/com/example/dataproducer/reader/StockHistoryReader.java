package com.example.dataproducer.reader;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.exception.CSVReadException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.dataproducer.constants.DataProducerConstants.*;
import static com.example.dataproducer.constants.MessagingConstants.CSV_READ_ERROR_MSG;

/**
 * @author irfan.nagoo
 */
public class StockHistoryReader implements CSVReader<StockHistoryObject> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("[yyyy-MM-dd]" +
            "[dd-MM-yyyy]");

    private final String ticker;
    private final MultipartFile file;

    public StockHistoryReader(String ticker, MultipartFile file) {
        this.ticker = ticker;
        this.file = file;
    }

    public List<StockHistoryObject> parse() {
        List<StockHistoryObject> historyObjectList = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser parser = CSVFormat.Builder.create(CSVFormat.EXCEL)
                    .setHeader().build()
                    .parse(reader);
            for (CSVRecord record : parser) {
                StockHistoryObject stockHistory = new StockHistoryObject();
                stockHistory.setName(ticker);
                stockHistory.setDate(toLocalDate(record.get(DATE)));
                stockHistory.setOpenPrice(new BigDecimal(record.get(OPEN)));
                stockHistory.setHighPrice(new BigDecimal(record.get(HIGH)));
                stockHistory.setLowPrice(new BigDecimal(record.get(LOW)));
                stockHistory.setClosePrice(new BigDecimal(record.get(CLOSE)));
                stockHistory.setVolume(Long.valueOf(record.get(VOLUME)));
                historyObjectList.add(stockHistory);
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new CSVReadException(CSV_READ_ERROR_MSG, e);
        }
        return historyObjectList;
    }

    private static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, FORMATTER);
    }
}
