package com.cfhtest.coinapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cfhtest.coinapp.core.exception.BusinessException;
import com.cfhtest.coinapp.entity.Currency;
import com.cfhtest.coinapp.entity.CurrencyHist;
import com.cfhtest.coinapp.form.CurrencyForm;
import com.cfhtest.coinapp.model.CoinDeskModel;
import com.cfhtest.coinapp.model.CoinDeskModel.CurrencyInfo;
import com.cfhtest.coinapp.service.dao.CurrencyHistRepository;
import com.cfhtest.coinapp.service.dao.CurrencyRepository;

/**
 * 測試 CoinDeskService 類別
 * 1. 測試 parseCoinDeskData 方法（成功）
 * 2. 測試 parseCoinDeskData 方法（解析失敗）
 * 3. 測試 updateCurrencyRates 方法（成功）
 * 4. 測試 updateCurrencyRates 方法（更新失敗）
 */
@ExtendWith(MockitoExtension.class)
public class CoinDeskServiceTest {

    @Mock
    private CurrencyLabelService currencyLabelService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyHistRepository currencyHistRepository;

    @Mock
    private CurrencyService currencyService;

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
    
    @Test
    void testUpdateCurrencyRates_Success() {
        // 模擬 CoinDeskModel 資料
        CoinDeskModel mockModel = new CoinDeskModel();
        mockModel.setUpdateTime("2023/01/01 00:00:00");

        CurrencyInfo usdInfo = new CurrencyInfo();
        usdInfo.setCode("USD");
        usdInfo.setRate("1.0");
        usdInfo.setRateFloat(1.0);
        usdInfo.setLabel("美元");

        CurrencyInfo eurInfo = new CurrencyInfo();
        eurInfo.setCode("EUR");
        eurInfo.setRate("0.85");
        eurInfo.setRateFloat(0.85);
        eurInfo.setLabel("歐元");

        mockModel.setCurrencies(Arrays.asList(usdInfo, eurInfo));

        // 模擬 parseCoinDeskData() 的行為
        doReturn(mockModel).when(coinDeskService).parseCoinDeskData();

        // 模擬 CurrencyRepository 的行為
        Currency existingCurrency = new Currency();
        existingCurrency.setCode("USD");
        existingCurrency.setRate("0.9");
        existingCurrency.setRateFloat(0.9);
        when(currencyRepository.findById("USD")).thenReturn(Optional.of(existingCurrency));
        when(currencyRepository.findById("EUR")).thenReturn(Optional.empty());

        // 呼叫要測試的方法
        coinDeskService.updateCurrencyRates();

        // 驗證更新現有的貨幣資料
        verify(currencyRepository, times(1)).save(argThat(currency -> 
            "USD".equals(currency.getCode()) &&
            "1.0".equals(currency.getRate()) &&
            currency.getRateFloat() == 1.0
        ));

        // 驗證新增新的貨幣資料
        verify(currencyService, times(1)).save(argThat(form -> 
            "EUR".equals(form.getCode()) &&
            "0.85".equals(form.getRate()) &&
            form.getRateFloat() == 0.85 &&
            "歐元".equals(form.getLabel())
        ));

        // 驗證新增匯率歷史紀錄
        verify(currencyHistRepository, times(2)).save(argThat(hist -> 
            ("USD".equals(hist.getCode()) && hist.getRateFloat() == 1.0) ||
            ("EUR".equals(hist.getCode()) && hist.getRateFloat() == 0.85)
        ));
    }

    @Test
    void testUpdateCurrencyRates_Failure() {
        // 模擬 parseCoinDeskData() 拋出例外
        doThrow(new BusinessException("解析 CoinDesk 資料失敗")).when(coinDeskService).parseCoinDeskData();

        // 呼叫要測試的方法並驗證例外
        BusinessException exception = assertThrows(BusinessException.class, () -> coinDeskService.updateCurrencyRates());
        assertEquals("解析 CoinDesk 資料失敗", exception.getMessage());

        // 驗證沒有進行任何儲存操作
        verify(currencyRepository, times(0)).save(any(Currency.class));
        verify(currencyService, times(0)).save(any(CurrencyForm.class));
        verify(currencyHistRepository, times(0)).save(any(CurrencyHist.class));

        // 確保 parseCoinDeskData() 被呼叫一次
        verify(coinDeskService, times(1)).parseCoinDeskData();
    }

}
