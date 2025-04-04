package com.cfhtest.coinapp.service.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.cfhtest.coinapp.model.Currency;

@RepositoryRestResource
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    
    @Override
    Optional<Currency> findById(Integer id);
    
    @Override
    Page<Currency> findAll(Pageable pageable);

    @Override
    <S extends Currency> S save(S entity);
    // 可以運作？

    @Override
    void delete(Currency entity);

}