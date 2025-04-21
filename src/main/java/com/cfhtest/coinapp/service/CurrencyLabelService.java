package com.cfhtest.coinapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfhtest.coinapp.service.dao.CurrencyLabelRepository;

/**
 * 貨幣名稱服務：
 * 提供根據貨幣代碼取得貨幣名稱的功能
 */
@Service
public class CurrencyLabelService {
    
    @Autowired
    private CurrencyLabelRepository currencyLabelRepository;

    /**
     * 根據貨幣代碼取得貨幣名稱
     * @param code 貨幣代碼
     * @return 貨幣名稱
     */
    public String getCurrencyLabel(String code) {
        return currencyLabelRepository.findById(code).map(label -> label.getLabel()).orElse("未知");
    }

}
