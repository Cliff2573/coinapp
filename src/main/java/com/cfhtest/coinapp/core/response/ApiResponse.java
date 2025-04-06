package com.cfhtest.coinapp.core.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = true;
        res.data = data;
        return res;
    }

    public static <T> ApiResponse<T> error(String errorMsg) {
        ApiResponse<T> res = new ApiResponse<>();
        res.success = false;
        res.error = errorMsg;
        return res;
    }

}
