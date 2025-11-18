package com.yubolee.coupon.template.parser.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SentinelOriginalParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        log.info("request={}, header={}",httpServletRequest.getParameterMap(), httpServletRequest.getHeaderNames());
        return httpServletRequest.getHeader("SentinelSource");
    }
}
