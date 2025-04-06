package com.cfhtest.coinapp.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CurrencyForm {

    @NotBlank(message = "代碼不得為空")
    @Schema(description = "貨幣代碼，例如 USD、EUR", example = "NTD")
    private String code;

    @NotBlank(message = "貨幣符號不得為空")
    @Schema(description = "貨幣符號，例如 $、€", example = "NT$")
    private String symbol;

    @NotBlank(message = "匯率不得為空")
    @Schema(description = "匯率（字串格式）", example = "30,123.9876")
    private String rate;

    @NotBlank(message = "貨幣說明不得為空")
    @Schema(description = "貨幣說明", example = "New Taiwan dollar")
    private String description;
    
    @NotNull(message = "匯率不得為空")
    @JsonProperty("rate_float")
    @Schema(description = "匯率（數值格式）", example = "30123.9876")
    private Double rateFloat;

    @NotBlank(message = "貨幣中文名稱不得為空")
    @Schema(description = "貨幣中文名稱", example = "新台幣")
    private String label;

}
