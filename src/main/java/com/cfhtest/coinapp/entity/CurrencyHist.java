package com.cfhtest.coinapp.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Schema(description = "貨幣資料更新紀錄")
public class CurrencyHist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主鍵")
    private int id;

    @Schema(description = "貨幣代碼，例如 USD、EUR")
    private String code;
    
    @JsonProperty("rate_float")
    @Schema(description = "匯率（數值格式）")
    private double rateFloat;
    
    @Schema(description = "更新時間")
    private Date updateDttm;

}
