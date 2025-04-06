package com.cfhtest.coinapp.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "currency_l")
public class CurrencyLabel {
    
    @Id
    @Schema(description = "貨幣代碼，例如 USD、EUR")
    private String code;

    @Schema(description = "貨幣中文名稱")
    private String label;

}
