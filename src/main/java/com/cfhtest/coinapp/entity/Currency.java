package com.cfhtest.coinapp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Schema(description = "貨幣資訊")
public class Currency {

    @Id
    @Schema(description = "貨幣代碼，例如 USD、EUR")
    private String code;

    @Schema(description = "貨幣符號，例如 $、€")
    private String symbol;

    @Schema(description = "匯率（字串格式）")
    private String rate;

    @Schema(description = "貨幣說明")
    private String description;
    
    @JsonProperty("rate_float")
    @Schema(description = "匯率（數值格式）")
    private double rateFloat;

}
