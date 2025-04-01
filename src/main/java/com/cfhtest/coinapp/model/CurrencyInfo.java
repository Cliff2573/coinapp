package com.cfhtest.coinapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CurrencyInfo {

    @Id
    private Integer coinSn;
    private String code;
    private String symbol;
    private String rate;
    private String description;
    
    @JsonProperty("rate_float")
    private double rateFloat;

}
