package com.cfhtest.coinapp.core.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;
    private String traceId;

    public static <T> ApiResponse<T> success(T data, String traceId) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = true;
        res.data = data;
        res.traceId = traceId;

        return res;
    }

    public static <T> ApiResponse<T> error(String errorMsg, String traceId) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = false;
        res.error = errorMsg;
        res.traceId = traceId;

        return res;
    }

}
