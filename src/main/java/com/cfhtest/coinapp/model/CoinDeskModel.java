package com.cfhtest.coinapp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CoinDeskModel {

    @Schema(description = "更新時間")
    private String updateTime;

    @Schema(description = "貨幣資訊清單")
    private List<CurrencyInfo> currencies;

    @Data
    public static class CurrencyInfo {
        
        @Schema(description = "貨幣代碼")
        private String code;
        
        @Schema(description = "貨幣中文名稱")
        private String label;

        @Schema(description = "匯率（字串格式）")
        private String rate;

        @Schema(description = "貨幣匯率")
        @JsonProperty("rate_float")
        private double rateFloat;
    }
    
}