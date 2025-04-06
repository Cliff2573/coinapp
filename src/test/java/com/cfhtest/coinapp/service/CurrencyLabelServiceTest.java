package com.cfhtest.coinapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cfhtest.coinapp.entity.CurrencyLabel;
import com.cfhtest.coinapp.service.dao.CurrencyLabelRepository;

/**
 * 測試 CurrencyLabelService 類別
 * 1. 測試 getCurrencyLabel 方法（成功）
 * 2. 測試 getCurrencyLabel 方法（查無資料）
 */
@ExtendWith(MockitoExtension.class)
public class CurrencyLabelServiceTest {

    @Mock
    private CurrencyLabelRepository currencyLabelRepository;

    @InjectMocks
    private CurrencyLabelService currencyLabelService;

    /**
     * 測試 getCurrencyLabel 方法（成功）
     * 1. 模擬資料庫查詢成功，返回貨幣代碼和名稱
     * 2. 驗證返回的貨幣名稱是否正確
     */
    @Test
    void testGetCurrencyLabel_Success() {
        // 模擬資料
        String code = "USD";
        String label = "美元";

        CurrencyLabel currencyLabel = new CurrencyLabel();
        currencyLabel.setCode(code);
        currencyLabel.setLabel(label);

        when(currencyLabelRepository.findById(code)).thenReturn(Optional.of(currencyLabel));

        // 呼叫要測試的 method
        String result = currencyLabelService.getCurrencyLabel(code);

        // 斷言
        assertNotNull(result);
        assertEquals("美元", result);

        // 驗證 repository 的互動
        verify(currencyLabelRepository, times(1)).findById(code);
    }

    /**
     * 測試 getCurrencyLabel 方法（查無資料）
     * 1. 模擬資料庫查詢失敗，返回空的 Optional
     * 2. 驗證返回的貨幣名稱是否為 "未知"
     */
    @Test
    void testGetCurrencyLabel_NotFound() {
        // 模擬資料不存在
        String code = "INVALID";
        when(currencyLabelRepository.findById(code)).thenReturn(Optional.empty());

        // 呼叫要測試的 method
        String result = currencyLabelService.getCurrencyLabel(code);

        // 斷言
        assertNotNull(result);
        assertEquals("未知", result);

        // 驗證 repository 的互動
        verify(currencyLabelRepository, times(1)).findById(code);
    }
    
}
