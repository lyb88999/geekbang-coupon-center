package com.yubolee.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponInfo {

    private Long id;

    // 优惠券模板id
    private Long templateId;

    // 用户id
    private Long userId;

    // 商户id
    private Long shopId;

    // 优惠券状态
    private Integer status;

    // 优惠券模版
    private CouponTemplateInfo template;
}
