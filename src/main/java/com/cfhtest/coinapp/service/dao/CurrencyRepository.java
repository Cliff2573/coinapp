package com.cfhtest.coinapp.service.dao;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfhtest.coinapp.model.CurrencyInfo;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyInfo, Integer> {

    // List<String> findAllCurrencies();

    // String findCurrencyByCode(String code);

    // void saveCurrency(String currency);

    // void deleteCurrencyByCode(String code);

}