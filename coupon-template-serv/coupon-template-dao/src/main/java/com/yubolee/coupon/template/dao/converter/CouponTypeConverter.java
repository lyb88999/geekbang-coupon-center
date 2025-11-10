package com.yubolee.coupon.template.dao.converter;

import com.yubolee.coupon.template.api.enums.CouponType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CouponTypeConverter implements AttributeConverter<CouponType, String> {
    @Override
    public String convertToDatabaseColumn(CouponType couponType) {
        return couponType.getCode();
    }

    @Override
    public CouponType convertToEntityAttribute(String s) {
        return CouponType.convert(s);
    }
}
