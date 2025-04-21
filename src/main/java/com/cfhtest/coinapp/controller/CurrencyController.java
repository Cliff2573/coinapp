package com.cfhtest.coinapp.controller;

import com.cfhtest.coinapp.form.CurrencyForm;
import com.cfhtest.coinapp.model.CurrencyModel;
import com.cfhtest.coinapp.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "貨幣 API", description = "提供貨幣資料查詢與操作功能")
@Validated
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Operation(summary = "取得所有貨幣資料")
    @GetMapping
    public Page<CurrencyModel> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return currencyService.getAll(page, size, sortDirection);
    }

    @Operation(summary = "根據 ID 查詢單一貨幣資料")
    @GetMapping("/{code}")
    public CurrencyModel getByCode(
        @PathVariable 
        @Pattern(regexp = "^[A-Z]{3}$", message = "代碼格式不正確，需為三碼大寫英文字母") 
        String code
    ) {
        return currencyService.getByCode(code);
    }

    @Operation(summary = "新增或更新一筆貨幣資料")
    @PostMapping
    public CurrencyModel save(@Valid @RequestBody CurrencyForm currencyForm) {
        return currencyService.save(currencyForm);
    }

    @Operation(summary = "刪除一筆貨幣資料 (by Code)")
    @DeleteMapping("/{code}")
    public void deleteByCode(
        @PathVariable 
        @Pattern(regexp = "^[A-Z]{3}$", message = "代碼格式不正確，需為三碼大寫英文字母")
        String code
    ) {
        currencyService.deleteByCode(code);
    }
    
}