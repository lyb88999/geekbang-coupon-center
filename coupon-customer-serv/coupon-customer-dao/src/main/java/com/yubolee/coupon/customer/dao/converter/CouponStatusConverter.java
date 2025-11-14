package com.yubolee.coupon.customer.dao.converter;

import com.yubolee.coupon.customer.api.enums.CouponStatus;
import jakarta.persistence.AttributeConverter;

public class CouponStatusConverter implements AttributeConverter<CouponStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer code) {
        return CouponStatus.convert(code);
    }
}
