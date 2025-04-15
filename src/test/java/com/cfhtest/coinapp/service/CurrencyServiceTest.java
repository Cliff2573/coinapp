package com.cfhtest.coinapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.cfhtest.coinapp.core.exception.BusinessException;
import com.cfhtest.coinapp.entity.Currency;
import com.cfhtest.coinapp.entity.CurrencyHist;
import com.cfhtest.coinapp.entity.CurrencyLabel;
import com.cfhtest.coinapp.entity.CurrencyView;
import com.cfhtest.coinapp.form.CurrencyForm;
import com.cfhtest.coinapp.model.CurrencyModel;
import com.cfhtest.coinapp.service.dao.CurrencyHistRepository;
import com.cfhtest.coinapp.service.dao.CurrencyLabelRepository;
import com.cfhtest.coinapp.service.dao.CurrencyRepository;
import com.cfhtest.coinapp.service.dao.CurrencyViewRepository;
import lombok.extern.slf4j.Slf4j;


/**
 * 測試 CurrencyService 類別
 * 1. 測試 getAll 方法（成功）
 * 2. 測試 getAll 方法（查無資料）
 * 3. 測試 getByCode 方法（成功）
 * 4. 測試 getByCode 方法（查無資料）
 * 5. 測試 save 方法（成功）
 * 6. 測試 save 方法（失敗）
 * 7. 測試 deleteByCode 方法（成功）
 * 8. 測試 deleteByCode 方法（失敗）
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private MessageSource messageSource;  // 模擬 MessageSource

    @Mock
    private CurrencyRepository currencyRepository;  // 模擬 CurrencyRepository

    @Mock
    private CurrencyLabelRepository currencyLabelRepository;  // 模擬 CurrencyLabelRepository

    @Mock
    private CurrencyViewRepository currencyViewRepository;  // 模擬 CurrencyViewRepository

    @Mock
    private CurrencyHistRepository currencyHistRepository;  // 模擬 CurrencyHistRepository

    @InjectMocks
    private CurrencyService currencyService;    // 被測試的 CurrencyService 服務

    /**
     * 1. 測試 getAll 方法（成功）
     * 當有資料時，應回傳正確的分頁結果
     */
    @Test
    void testGetAll_Success() {
        // 模擬資料
        int page = 0;
        int size = 10;
        String sortDirection = "asc";
        CurrencyView mockCurrencyView = new CurrencyView();
        mockCurrencyView.setCode("USD");
        mockCurrencyView.setSymbol("$");
        mockCurrencyView.setRate("1.0");
        mockCurrencyView.setDescription("US Dollar");
        mockCurrencyView.setRateFloat(1.0f);
        mockCurrencyView.setLabel("美元");

        List<CurrencyView> mockCurrencyViewList = List.of(mockCurrencyView);
        Page<CurrencyView> mockPage = new PageImpl<>(mockCurrencyViewList);

        // 模擬 repository 回傳值
        when(currencyViewRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "code"))))
            .thenReturn(mockPage);

        // 呼叫要測試的 method
        Page<CurrencyModel> result = currencyService.getAll(page, size, sortDirection);

        // 斷言
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        CurrencyModel currencyModel = result.getContent().get(0);
        assertEquals("USD", currencyModel.getCode());
        assertEquals("$", currencyModel.getSymbol());
        assertEquals("1.0", currencyModel.getRate());
        assertEquals("US Dollar", currencyModel.getDescription());
        assertEquals(1.0f, currencyModel.getRateFloat());
        assertEquals("美元", currencyModel.getLabel());

        verify(currencyViewRepository, times(1)).findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "code")));
    }

    /**
     * 2. 測試 getAll 方法（無資料）
     * 當沒有資料時，應回傳空的分頁結果
     */
    @Test
    void testGetAll_NoData() {
        // 模擬資料
        int page = 0;
        int size = 10;
        String sortDirection = "asc";

        // 模擬 repository 回傳空的分頁結果
        Page<CurrencyView> mockPage = new PageImpl<>(Collections.emptyList());
        when(currencyViewRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "code"))))
            .thenReturn(mockPage);

        // 呼叫要測試的 method
        Page<CurrencyModel> result = currencyService.getAll(page, size, sortDirection);

        // 斷言
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(currencyViewRepository, times(1)).findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "code")));
    }
    
    /**
     * 3. 測試 getByCode 方法（成功）
     * 當查詢的貨幣存在時，應回傳正確的 CurrencyModel
     */
    @Test
    void testGetByCode_CurrencyExists() {
        // 模擬資料
        String code = "USD";
        CurrencyView mockCurrencyView = new CurrencyView();
        mockCurrencyView.setCode(code);
        mockCurrencyView.setSymbol("$");
        mockCurrencyView.setRate("1.0");
        mockCurrencyView.setDescription("US Dollar");
        mockCurrencyView.setRateFloat(1.0f);
        mockCurrencyView.setLabel("美元");
        
        // 模擬 repository 回傳值
        when(currencyViewRepository.findById(code)).thenReturn(Optional.of(mockCurrencyView));

        // 呼叫要測試的 method
        CurrencyModel result = currencyService.getByCode(code);

        // 斷言
        assertNotNull(result);
        assertEquals(code, result.getCode());
        assertEquals("$", result.getSymbol());
        assertEquals("1.0", result.getRate());
        assertEquals("US Dollar", result.getDescription());
        assertEquals(1.0f, result.getRateFloat());
        assertEquals("美元", result.getLabel());

        verify(currencyViewRepository, times(1)).findById(code);
    }

    /**
     * 4. 測試 getByCode 方法（失敗）
     * 當查詢的貨幣不存在時，應回傳 null
     */
    @Test
    void testGetByCode_CurrencyDoesNotExist() {
        // 模擬資料
        String code = "INVALID";
        when(currencyViewRepository.findById(code)).thenReturn(Optional.empty());

        // 呼叫要測試的 method 並驗證例外
        BusinessException exception = assertThrows(BusinessException.class, () -> currencyService.getByCode(code));
        assertEquals(messageSource.getMessage("error.notfound", new Object[] { code }, Locale.getDefault()), exception.getMessage());

        verify(currencyViewRepository, times(1)).findById(code);
    }


    /**
     * 5. 測試 save 方法（成功）
     * 當儲存貨幣資料成功時，應回傳正確的 CurrencyModel
     */
    @Test
    void testSave_Success() {
        // 模擬輸入的 CurrencyForm
        CurrencyForm currencyForm = new CurrencyForm();
        currencyForm.setCode("USD");
        currencyForm.setSymbol("$");
        currencyForm.setRate("1.0");
        currencyForm.setDescription("US Dollar");
        currencyForm.setRateFloat(1.0);
        currencyForm.setLabel("美元");

        // 模擬儲存的 Currency 實體
        Currency mockCurrency = new Currency();
        mockCurrency.setCode("USD");
        mockCurrency.setSymbol("$");
        mockCurrency.setRate("1.0");
        mockCurrency.setDescription("US Dollar");
        mockCurrency.setRateFloat(1.0);

        // 模擬儲存的 CurrencyLabel 實體
        CurrencyLabel mockCurrencyLabel = new CurrencyLabel();
        mockCurrencyLabel.setCode("USD");
        mockCurrencyLabel.setLabel("美元");

        // 模擬儲存的 CurrencyHist 實體
        CurrencyHist mockCurrencyHist = new CurrencyHist();
        mockCurrencyHist.setCode("USD");
        mockCurrencyHist.setRateFloat(1.0);
        mockCurrencyHist.setUpdateDttm(new Date(1672531200000L));

        // 模擬 repository 的行為
        when(currencyRepository.save(any(Currency.class))).thenReturn(mockCurrency);
        when(currencyLabelRepository.save(any(CurrencyLabel.class))).thenReturn(mockCurrencyLabel);
        when(currencyHistRepository.save(any(CurrencyHist.class))).thenReturn(mockCurrencyHist);

        // 呼叫要測試的 method
        CurrencyModel result = currencyService.save(currencyForm);

        // 斷言
        assertNotNull(result);
        assertEquals("USD", result.getCode());
        assertEquals("$", result.getSymbol());
        assertEquals("1.0", result.getRate());
        assertEquals("US Dollar", result.getDescription());
        assertEquals(1.0f, result.getRateFloat());
        assertEquals("美元", result.getLabel());
        assertNotNull(result.getUpdateDttm());
        
        // 驗證 repository 的互動
        verify(currencyRepository, times(1)).save(any(Currency.class));
        verify(currencyLabelRepository, times(1)).save(any(CurrencyLabel.class));
        verify(currencyHistRepository, times(1)).save(any(CurrencyHist.class));
    }

    /**
     * 6. 測試 save 方法（失敗）
     * 當儲存貨幣資料失敗時，應回傳 null
     */
    @Test
    void testSave_Failure() {
        // 模擬輸入的 CurrencyForm
        CurrencyForm currencyForm = new CurrencyForm();
        currencyForm.setCode("USD");
        currencyForm.setSymbol("$");
        currencyForm.setRate("1.0");
        currencyForm.setDescription("US Dollar");
        currencyForm.setRateFloat(1.0);
        currencyForm.setLabel("美元");

        // 模擬 repository 拋出例外
        when(currencyRepository.save(any(Currency.class))).thenThrow(new RuntimeException("Database error"));

        // 呼叫要測試的 method 並驗證例外
        RuntimeException exception = assertThrows(RuntimeException.class, () -> currencyService.save(currencyForm));
        assertEquals("Database error", exception.getMessage());

        // 驗證 repository 的互動
        verify(currencyRepository, times(1)).save(any(Currency.class));
        verify(currencyLabelRepository, never()).save(any(CurrencyLabel.class)); // 確保第二個儲存未被呼叫
    }

    /**
     * 7. 測試 deleteByCode 方法（成功）
     * 當刪除貨幣資料成功時，應無例外發生
     */
    @Test
    void testDeleteByCode_Success() {
        // 模擬資料存在
        String code = "USD";
        when(currencyRepository.existsById(code)).thenReturn(true);

        // 呼叫要測試的 method
        assertDoesNotThrow(() -> currencyService.deleteByCode(code));

        // 驗證 repository 的互動
        verify(currencyRepository, times(1)).existsById(code);
        verify(currencyRepository, times(1)).deleteById(code);
    }

    /**
     * 8. 測試 deleteByCode 方法（失敗）
     * 當刪除貨幣資料失敗時，應拋出 BusinessException
     */
    @Test
    void testDeleteByCode_NotFound() {
        // 模擬資料不存在
        String code = "INVALID";
        when(currencyRepository.existsById(code)).thenReturn(false);

        // 呼叫要測試的 method 並驗證例外
        BusinessException exception = assertThrows(BusinessException.class, () -> currencyService.deleteByCode(code));
        assertEquals(messageSource.getMessage("error.notfound.delete", new Object[] { code }, Locale.getDefault()), exception.getMessage());

        // 驗證 repository 的互動
        verify(currencyRepository, times(1)).existsById(code);
        verify(currencyRepository, never()).deleteById(anyString()); // 確保 deleteById 未被呼叫
    }


}