package com.cfhtest.coinapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cfhtest.coinapp.model.CoinDeskModel;
import com.cfhtest.coinapp.service.CoinDeskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "貨幣外部 API", description = "提供外接 CoinDesk API 結果")
@Slf4j
@RestController
public class CoinDeskController {

    @Autowired
    private CoinDeskService coinDeskService;

    /**
     * 取得 CoinDesk API 的資料
     * @return CoinDeskModel 解析後的資料
     */
    @Operation(summary = "處理 CoinDesk API 的資料")
    @GetMapping("/coindesk")
    public CoinDeskModel getCoinDeskData() {

        try {
            return coinDeskService.parseCoinDeskData();
        } catch (Exception e) {
            log.error("Error while processing CoinDesk API data: {}", e.getMessage());
            return null;
        }

    }

}