package com.yubolee.coupon.customer.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CouponStatus {

    AVAILABLE("未使用", 1),
    USED("已使用", 2),
    INACTIVE("已过期", 3);

    private String desc;
    private Integer code;

    public static CouponStatus convert(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElse(null);
    }
}
