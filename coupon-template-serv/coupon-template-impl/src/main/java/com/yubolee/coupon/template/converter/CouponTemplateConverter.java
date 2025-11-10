package com.yubolee.coupon.template.converter;

import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import com.yubolee.coupon.template.dao.eneity.CouponTemplate;

public class CouponTemplateConverter {

    public static CouponTemplateInfo convertTemplateInfo(CouponTemplate template) {
        return CouponTemplateInfo.builder()
                .id(template.getId())
                .name(template.getName())
                .desc(template.getDescription())
                .type(template.getCategory().getCode())
                .shopId(template.getShopId())
                .rule(template.getRule())
                .available(template.getAvailable())
                .build();
    }
}
