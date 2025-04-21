package com.cfhtest.coinapp.core.exception;

import java.util.stream.Collectors;

import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cfhtest.coinapp.core.response.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * GlobalExceptionHandler 是一個全域的例外處理器，負責處理應用程式中的各種例外情況。
 * 當發生例外時，這個類別會捕捉到例外並回傳一個統一格式的 ApiResponse 物件。
 * 這樣可以確保 API 的回應格式一致，並且方便前端進行錯誤處理。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private String getTraceId() {
        return MDC.get("traceId");
    }

    /**
     * 這個方法會處理所有的 BusinessException，並回傳一個 ApiResponse 物件
     * @param ex BusinessException 例外物件
     * @return ApiResponse 物件
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        log.error("BusinessException occurred: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage(), getTraceId()));
    }

    /**
     * 這個方法會處理所有的 MethodArgumentNotValidException，並回傳一個 ApiResponse 物件
     * @param ex MethodArgumentNotValidException 例外物件
     * @return ApiResponse 物件
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                            .getAllErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(ApiResponse.error(errorMsg, getTraceId()));
    }

    /**
     * 這個方法會處理所有的 ConstraintViolationException，並回傳一個 ApiResponse 物件
     * @param ex ConstraintViolationException 例外物件
     * @return ApiResponse 物件
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMsg = ex.getConstraintViolations()
                            .stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(ApiResponse.error(errorMsg, getTraceId()));
    }

    /**
     * 這個方法會處理所有的 Exception，並回傳一個 ApiResponse 物件
     * （i.e. 未詳細定義到的 exception）
     * @param ex Exception 例外物件
     * @return ApiResponse 物件
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex.getMessage(), getTraceId()));
    }

}