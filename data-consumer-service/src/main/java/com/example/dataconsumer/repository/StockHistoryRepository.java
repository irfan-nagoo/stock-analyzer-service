package com.example.dataconsumer.repository;

import com.example.dataconsumer.entity.StockHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author irfan.nagoo
 */

@Repository
public interface StockHistoryRepository extends MongoRepository<StockHistory, Long> {

    List<StockHistory> findByName(String name, Pageable pageable);

    List<StockHistory> findByNameAndDateBetween(String name, LocalDate startDate, LocalDate endDate, Sort sort);

    void deleteByNameAndDateBetween(String name, LocalDate startDate, LocalDate endDate);
}
