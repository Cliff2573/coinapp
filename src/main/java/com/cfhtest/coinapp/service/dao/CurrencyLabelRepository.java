package com.cfhtest.coinapp.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfhtest.coinapp.entity.CurrencyLabel;

@Repository
public interface CurrencyLabelRepository extends JpaRepository<CurrencyLabel, String> {

}