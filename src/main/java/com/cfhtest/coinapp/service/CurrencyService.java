package com.cfhtest.coinapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cfhtest.coinapp.core.exception.BusinessException;
import com.cfhtest.coinapp.entity.Currency;
import com.cfhtest.coinapp.entity.CurrencyLabel;
import com.cfhtest.coinapp.entity.CurrencyView;
import com.cfhtest.coinapp.form.CurrencyForm;
import com.cfhtest.coinapp.model.CurrencyModel;
import com.cfhtest.coinapp.service.dao.CurrencyLabelRepository;
import com.cfhtest.coinapp.service.dao.CurrencyRepository;
import com.cfhtest.coinapp.service.dao.CurrencyViewRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrencyService {
    
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyLabelRepository currencyLabelRepository;

    @Autowired
    private CurrencyViewRepository currencyViewRepository;

    /**
     * 取得所有貨幣資料（分頁）
     */
    public Page<CurrencyModel> getAll(int page, int size, String sortDirection) {
        // 確保排序方向是 asc 或 desc
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // 根據預設的 "code" 欄位進行排序
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "code"));

        Page<CurrencyView> viewPage = currencyViewRepository.findAll(pageable);

        return viewPage.map(this::toModel);
    }

    /**
     * 根據 CODE 查詢單一貨幣資料
     */
    public CurrencyModel getByCode(String code) {
        CurrencyView currencyView = currencyViewRepository.findById(code)
                .orElseThrow(() -> new BusinessException("找不到指定的貨幣資料: " + code));
        return toModel(currencyView);
    }

    /**
     * 新增或更新一筆貨幣資料
     */
    public CurrencyModel save(CurrencyForm currencyForm) {

        // 儲存貨幣資料
        Currency currency = new Currency();

        currency.setCode(currencyForm.getCode());
        currency.setSymbol(currencyForm.getSymbol());
        currency.setRate(currencyForm.getRate());
        currency.setDescription(currencyForm.getDescription());
        currency.setRateFloat(currencyForm.getRateFloat());

        Currency savedCurrency = currencyRepository.save(currency);

        // 儲存貨幣中文名稱
        CurrencyLabel currencyLabel = new CurrencyLabel();
        currencyLabel.setCode(savedCurrency.getCode());
        currencyLabel.setLabel(currencyForm.getLabel());

        CurrencyLabel savedCurrencyLabel = currencyLabelRepository.save(currencyLabel);

        // 組合回傳
        CurrencyModel currencyModel = toModel(savedCurrency, savedCurrencyLabel);

        return currencyModel;
    }

    /**
     * 刪除一筆貨幣資料 (by Code)
     */
    @Transactional
    public void deleteByCode(String code) {
        if (!currencyRepository.existsById(code)) {
            throw new BusinessException("找不到指定的貨幣資料，無法刪除: " + code);
        }
        currencyRepository.deleteById(code);
    }

    /**
     * 將 CurrencyView 轉換為 CurrencyModel
     */
    private CurrencyModel toModel(CurrencyView currencyView) {

        CurrencyModel currencyModel = new CurrencyModel();
        currencyModel.setCode(currencyView.getCode());
        currencyModel.setSymbol(currencyView.getSymbol());
        currencyModel.setRate(currencyView.getRate());
        currencyModel.setDescription(currencyView.getDescription());
        currencyModel.setRateFloat(currencyView.getRateFloat());
        currencyModel.setLabel(currencyView.getLabel());

        return currencyModel;
    }

    /**
     * 將 Currency + CurrencyLabel 轉換為 CurrencyModel
     */
    private CurrencyModel toModel(Currency currency, CurrencyLabel currencyLabel) {

        CurrencyModel currencyModel = new CurrencyModel();
        currencyModel.setCode(currency.getCode());
        currencyModel.setSymbol(currency.getSymbol());
        currencyModel.setRate(currency.getRate());
        currencyModel.setDescription(currency.getDescription());
        currencyModel.setRateFloat(currency.getRateFloat());

        currencyModel.setLabel(currencyLabel.getLabel());

        return currencyModel;
    }

}
