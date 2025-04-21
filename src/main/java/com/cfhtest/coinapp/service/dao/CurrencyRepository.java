package com.cfhtest.coinapp.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfhtest.coinapp.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {

}