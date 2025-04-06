package com.cfhtest.coinapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cfhtest.coinapp.core.exception.BusinessException;
import com.cfhtest.coinapp.model.CoinDeskModel;

@ExtendWith(MockitoExtension.class)
public class CoinDeskServiceTest {

    @Mock
    private CurrencyLabelService currencyLabelService;

    @Spy
    @InjectMocks
    private CoinDeskService coinDeskService;    // 被測試的 CurrencyService 服務

    @Test
    void testParseCoinDeskData_Success() throws Exception {

        // 模擬 getCurrentPrice() 回傳的 JSON 資料
        String mockJson = """
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
        doReturn(mockJson).when(coinDeskService).getCurrentPrice();

        // 模擬 CurrencyLabelService 的行為
        when(currencyLabelService.getCurrencyLabel("USD")).thenReturn("美元");
        when(currencyLabelService.getCurrencyLabel("GBP")).thenReturn("英鎊");
        when(currencyLabelService.getCurrencyLabel("EUR")).thenReturn("歐元");

        // 呼叫要測試的方法
        CoinDeskModel result = coinDeskService.parseCoinDeskData();

        // 斷言解析結果
        assertNotNull(result);
        assertEquals("2022/08/03 20:25:00", result.getUpdateTime());

        List<CoinDeskModel.CurrencyInfo> currencies = result.getCurrencies();
        assertEquals(3, currencies.size());

        CoinDeskModel.CurrencyInfo usd = currencies.stream().filter(c -> "USD".equals(c.getCode())).findFirst().orElse(null);
        assertNotNull(usd);
        assertEquals("美元", usd.getLabel());
        assertEquals(23342.0112, usd.getRateFloat());

        CoinDeskModel.CurrencyInfo gbp = currencies.stream().filter(c -> "GBP".equals(c.getCode())).findFirst().orElse(null);
        assertNotNull(gbp);
        assertEquals("英鎊", gbp.getLabel());
        assertEquals(19504.3978, gbp.getRateFloat());

        CoinDeskModel.CurrencyInfo eur = currencies.stream().filter(c -> "EUR".equals(c.getCode())).findFirst().orElse(null);
        assertNotNull(eur);
        assertEquals("歐元", eur.getLabel());
        assertEquals(22738.5269, eur.getRateFloat());

        // 驗證 CurrencyLabelService 的互動
        verify(currencyLabelService, times(1)).getCurrencyLabel("USD");
        verify(currencyLabelService, times(1)).getCurrencyLabel("GBP");
        verify(currencyLabelService, times(1)).getCurrencyLabel("EUR");
    }

    @Test
    void testParseCoinDeskData_ParseFailure() throws Exception {
        // 模擬 getCurrentPrice() 回傳的無效 JSON 資料
        String invalidJson = "INVALID_JSON";
        doReturn(invalidJson).when(coinDeskService).getCurrentPrice();

        // 呼叫要測試的方法並驗證例外
        BusinessException exception = assertThrows(BusinessException.class, () -> coinDeskService.parseCoinDeskData());
        assertTrue(exception.getMessage().contains("解析 CoinDesk API 資料時發生錯誤"));

        // 驗證 getCurrentPrice() 被呼叫一次
        verify(coinDeskService, times(1)).getCurrentPrice();
    }
    
}
