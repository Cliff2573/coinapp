package com.cfhtest.coinapp.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.cfhtest.coinapp.Utils.DateTimeUtils;
import com.cfhtest.coinapp.core.exception.BusinessException;
import com.cfhtest.coinapp.entity.Currency;
import com.cfhtest.coinapp.entity.CurrencyHist;
import com.cfhtest.coinapp.form.CurrencyForm;
import com.cfhtest.coinapp.model.CoinDeskApiResponse;
import com.cfhtest.coinapp.model.CoinDeskModel;
import com.cfhtest.coinapp.service.dao.CurrencyHistRepository;
import com.cfhtest.coinapp.service.dao.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoinDeskService {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRepository currencyRepository;
    
    @Autowired
    private CurrencyLabelService currencyLabelService;

    @Autowired
    private CurrencyHistRepository currencyHistRepository;


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
            CoinDeskApiResponse coinDeskApiResponse = mapper.readValue(getCurrentPrice(), CoinDeskApiResponse.class);

            // log 原始 JSON
            log.info("CoinDesk API response: {}", coinDeskApiResponse);

            // 格式化時間
            String formattedTime = ZonedDateTime
                .parse(coinDeskApiResponse.getTime().getUpdatedISO())
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

            // 幣別處理
            List<CoinDeskModel.CurrencyInfo> currencyList = coinDeskApiResponse.getBpi().values().stream()
                .map(apiCurrency -> {

                    log.info("幣別: {}", apiCurrency.getCode());

                    CoinDeskModel.CurrencyInfo info = new CoinDeskModel.CurrencyInfo();
                    info.setCode(apiCurrency.getCode());
                    info.setLabel(currencyLabelService.getCurrencyLabel(apiCurrency.getCode()));
                    info.setRate(apiCurrency.getRate());
                    info.setRateFloat(apiCurrency.getRate_float());
                    return info;
                }).collect(Collectors.toList());

            // 組合 CoinDeskModel
            CoinDeskModel response = new CoinDeskModel();
            response.setUpdateTime(formattedTime);
            response.setCurrencies(currencyList);
            
            return response;

        } catch (IOException | RuntimeException ex) {
            throw new BusinessException("解析 CoinDesk API 資料時發生錯誤：" + ex);
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // 每天凌晨 0 點執行一次
    public void updateCurrencyRates() {

        log.info("開始排程更新匯率資料...");
        
        CoinDeskModel model = parseCoinDeskData();

        Date updateDttm = DateTimeUtils.stringToDate(model.getUpdateTime());

        model.getCurrencies().forEach(info -> {

            String code = info.getCode();
            String rate = info.getRate();
            double rateFloat = info.getRateFloat();

            Currency existingCurrency = currencyRepository.findById(code).orElse(null);
            
            if (existingCurrency != null) {
                // 更新現有的貨幣資料
                Currency newCurrency = new Currency();
                BeanUtils.copyProperties(existingCurrency, newCurrency);

                newCurrency.setRate(rate);
                newCurrency.setRateFloat(rateFloat);

                currencyRepository.save(newCurrency);
            } 
            // 新增新的貨幣資料
            else {
                CurrencyForm newCurrencyForm = new CurrencyForm();

                newCurrencyForm.setCode(code);
                newCurrencyForm.setRate(rate);
                newCurrencyForm.setRateFloat(rateFloat);
                newCurrencyForm.setLabel(info.getLabel());
                
                currencyService.save(newCurrencyForm);
            }

            // 新增匯率歷史紀錄
            CurrencyHist currencyHist = new CurrencyHist();
            currencyHist.setCode(code);
            currencyHist.setRateFloat(rateFloat);
            currencyHist.setUpdateDttm(updateDttm);
            
            currencyHistRepository.save(currencyHist);
        });

        log.info("匯率更新完成。");
    }

}
