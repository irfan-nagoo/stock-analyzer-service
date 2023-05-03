package com.example.dataproducer.reader;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.exception.CSVReadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static com.example.dataproducer.constants.MessagingConstants.CSV_READ_ERROR_MSG;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StockHistoryReaderTest {

    private StockHistoryReader stockHistoryReader;


    @Test
    void parse() {
        MockMultipartFile file = new MockMultipartFile("APPL.csv", ("Date,Open,High,Low,Close,Volume\n" +
                "1999-11-18,30.713,33.754,27.002,29.702,66277506").getBytes());
        stockHistoryReader = new StockHistoryReader("APPL", file);
        List<StockHistoryObject> stockHistoryList = stockHistoryReader.parse();
        assertNotNull(stockHistoryList);
        assertEquals(1, stockHistoryList.size());
        assertEquals("1999-11-18", stockHistoryList.get(0).getDate().toString());
        assertEquals("29.702", stockHistoryList.get(0).getClosePrice().toString());
        assertEquals(66277506L, stockHistoryList.get(0).getVolume());
    }

    @Test
    void parse_Exception() {
        MockMultipartFile file = new MockMultipartFile("APPL.csv", ("Date,Invalid_Header\n" +
                "2022-11-18, 10.70").getBytes());
        stockHistoryReader = new StockHistoryReader("APPL", file);
        CSVReadException exception = assertThrows(CSVReadException.class,
                () -> stockHistoryReader.parse());
        assertEquals(CSV_READ_ERROR_MSG, exception.getMessage());
    }
}