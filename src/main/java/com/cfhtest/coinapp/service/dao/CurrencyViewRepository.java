package com.cfhtest.coinapp.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfhtest.coinapp.entity.CurrencyView;

@Repository
public interface CurrencyViewRepository extends JpaRepository<CurrencyView, String> {
    
}
