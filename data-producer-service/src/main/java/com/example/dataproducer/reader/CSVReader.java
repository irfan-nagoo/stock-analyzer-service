package com.example.dataproducer.reader;

import java.util.List;

/**
 * @author irfan.nagoo
 */

public interface CSVReader<T> {

    List<T> parse();

}
