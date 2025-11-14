package com.yubolee.coupon.customer.converter;


import com.yubolee.coupon.customer.dao.entity.Coupon;
import com.yubolee.coupon.template.api.beans.CouponInfo;

public class CouponConverter {
    public static CouponInfo convertToCoupon(Coupon coupon){
        return CouponInfo.builder()
                .id(coupon.getId())
                .status(coupon.getStatus().getCode())
                .template(coupon.getTemplateInfo())
                .shopId(coupon.getShopId())
                .userId(coupon.getUserId())
                .build();
    }
}
