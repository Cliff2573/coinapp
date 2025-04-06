package com.cfhtest.coinapp.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.cfhtest.coinapp.core.exception.BusinessException;
import com.cfhtest.coinapp.model.CoinDeskModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoinDeskService {
    
    @Autowired
    private CurrencyLabelService currencyLabelService;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 取得 CoinDesk API 的即時價格資料
     * @return JSON 字串
     */
    String getCurrentPrice() {

        // Coindesk API URL
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

        // 預設回傳的 JSON 資料
        String coindeskJson = "";
        
        // Mock 資料
        String coinDeskMock = """
            {
                "time": {
                    "updated": "Aug 3, 2022 20:25:00 UTC",
                    "updatedISO": "2022-08-03T20:25:00+00:00",
                    "updateduk": "Aug 3, 2022 at 21:25 BST"
                },
                "disclaimer": 
                    "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
                "chartName": 
                    "Bitcoin",
                "bpi": {
                    "USD": {
                        "code": "USD",
                        "symbol": "$",
                        "rate": "23,342.0112",
                        "description": "US Dollar",
                        "rate_float": 23342.0112
                    },
                    "GBP": {
                        "code": "GBP",
                        "symbol": "£",
                        "rate": "19,504.3978",
                        "description": "British Pound Sterling",
                        "rate_float": 19504.3978
                    },
                    "EUR": {
                        "code": "EUR",
                        "symbol": "€",
                        "rate": "22,738.5269",
                        "description": "Euro",
                        "rate_float": 22738.5269
                    }
                }
            }
        """;
        
        try {
            coindeskJson = restTemplate.getForObject(url, String.class);
        } 
        catch (ResourceAccessException e) {
            // 如果 API 不通，則回傳 mock 資料
            log.warn("無法連接 Coindesk API，使用 Mock Data 代替。");
            coindeskJson = coinDeskMock;
        }

        return coindeskJson;
    }

    /**
     * 解析 CoinDesk API 資料
     * @return CoinDeskModel
     */
    public CoinDeskModel parseCoinDeskData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(getCurrentPrice());

            // log API response
            log.info("CoinDesk API response: {}", root.toString());

            // 處理時間格式
            String updatedISO = root.path("time").path("updatedISO").asText();
            ZonedDateTime updatedTime = ZonedDateTime.parse(updatedISO);
            String formattedTime = updatedTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

            // 處理幣別資訊
            List<CoinDeskModel.CurrencyInfo> currencyList = new ArrayList<>();
            JsonNode bpi = root.path("bpi");

            bpi.fieldNames().forEachRemaining(code -> {
                JsonNode node = bpi.get(code);
                CoinDeskModel.CurrencyInfo info = new CoinDeskModel.CurrencyInfo();
                info.setCode(code);
                info.setLabel(currencyLabelService.getCurrencyLabel(code));
                info.setRateFloat(node.path("rate_float").asDouble());
                currencyList.add(info);
            });

            // 組合 CoinDeskModel
            CoinDeskModel response = new CoinDeskModel();
            response.setUpdateTime(formattedTime);
            response.setCurrencies(currencyList);
            
            return response;

        } catch (IOException | RuntimeException ex) {
            throw new BusinessException("解析 CoinDesk API 資料時發生錯誤：" + ex);
        }
    }

}
