package com.cfhtest.coinapp.core.config;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * RequestTraceFilter 是一個過濾器，用來處理請求的 traceId。
 * 它會在每個請求中檢查是否有提供 traceId，若沒有則自動生成一個新的 traceId。
 * 此外，它還會將 traceId 設置到響應的 header 中，以便於後續的請求追蹤。
 */
@Component
public class RequestTraceFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // 取得請求中的 traceId，若不存在則產生一個新的 UUID 作為 traceId
        String traceId = Optional.ofNullable(request.getHeader(TRACE_ID_HEADER))
                         .orElse(UUID.randomUUID().toString().replace("-", "").substring(0, 12));

        MDC.put("traceId", traceId); // 加入 slf4j 的 MDC，可以在 log 格式中使用
        response.setHeader(TRACE_ID_HEADER, traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("traceId"); // 清除避免 ThreadLocal 汙染
        }
    }
}