package com.cfhtest.coinapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Currency {

    @Id
    @GeneratedValue
    private Integer sn;

    private String code;

    private String symbol;

    private String rate;

    private String description;
    
    @Column(name = "rate_float")
    private double rateFloat;

}
