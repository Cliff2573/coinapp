package com.cfhtest.coinapp.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfhtest.coinapp.entity.CurrencyHist;

@Repository
public interface CurrencyHistRepository extends JpaRepository<CurrencyHist, Integer> {
    
}
