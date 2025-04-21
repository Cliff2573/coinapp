package com.cfhtest.coinapp.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CurrencyModel {

    @Schema(description = "貨幣代碼，例如 USD、EUR", example = "NTD")
    private String code;

    @Schema(description = "貨幣符號，例如 $、€", example = "NT$")
    private String symbol;

    @Schema(description = "匯率（字串格式）", example = "30,123.9876")
    private String rate;

    @Schema(description = "貨幣說明", example = "New Taiwan dollar")
    private String description;
    
    @JsonProperty("rate_float")
    @Schema(description = "匯率（數值格式）", example = "30123.9876")
    private double rateFloat;

    @Schema(description = "貨幣中文名稱", example = "新台幣")
    private String label;

    @Schema(description = "更新時間")
    private Date updateDttm;

}
