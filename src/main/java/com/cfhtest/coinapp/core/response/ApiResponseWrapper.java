package com.cfhtest.coinapp.core.response;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseWrapper implements ResponseBodyAdvice<Object> {

    /**
     * 判斷是否需要包裝 ApiResponse 物件
     * @param returnType 方法參數
     * @param converterType 轉換器類型
     * @return 是否需要包裝
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 不包裝 already wrapped 的 ApiResponse 或 Swagger 文件請求
        return !returnType.getParameterType().equals(ApiResponse.class)
               && !returnType.getDeclaringClass().getName().startsWith("org.springdoc");
    }

    /**
     * 包裝 ApiResponse 物件
     * @param body 原始回應物件
     * @param returnType 方法參數
     * @param selectedContentType 選擇的內容類型
     * @param selectedConverterType 選擇的轉換器類型
     * @param request 請求物件
     * @param response 回應物件
     * @return 包裝後的 ApiResponse 物件
     */
    @Override
    public Object beforeBodyWrite(
        Object body,
        MethodParameter returnType,
        MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType,
        ServerHttpRequest request,
        ServerHttpResponse response) {

        // 如果是 ApiResponse 物件，則不進行包裝
        if (body instanceof ApiResponse) {
            return body;
        }

        // 如果是 void 或 null，則回傳 ApiResponse.success(null)
        if (body == null) {
            return ApiResponse.success(null, MDC.get("traceId"));
        }
    
        // 如果是其他物件，則包裝成 ApiResponse 物件
        return ApiResponse.success(body, MDC.get("traceId"));
    }
}
