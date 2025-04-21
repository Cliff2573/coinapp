package com.cfhtest.coinapp.entity;

import org.hibernate.annotations.Immutable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Immutable
@Schema(description = "貨幣資訊（包含中文名稱）的 view")
public class CurrencyView {
    
    @Id
    @Schema(description = "貨幣代碼，例如 USD")
    private String code;

    @Schema(description = "貨幣符號，例如 $、€")
    private String symbol;

    @Schema(description = "匯率（字串格式）")
    private String rate;

    @Schema(description = "貨幣說明")
    private String description;

    @Schema(description = "匯率（數值格式）")
    private double rateFloat;

    @Schema(description = "貨幣中文名稱")
    private String label;

}
