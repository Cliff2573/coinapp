package com.cfhtest.coinapp.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CoinDeskApiResponse {
    private Time time;
    private Map<String, Currency> bpi;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Time {
        private String updatedISO;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Currency {
        private String code;
        private String rate;
        private double rate_float;
    }
}
