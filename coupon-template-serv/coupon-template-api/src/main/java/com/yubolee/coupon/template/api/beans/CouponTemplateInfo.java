package com.yubolee.coupon.template.api.beans;

import com.yubolee.coupon.template.api.beans.rules.TemplateRule;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponTemplateInfo {

    private Long id;
    @NotNull
    private String name; // 优惠券名称
    @NotNull
    private String desc; // 优惠券描述
    @NotNull
    private String type; // 优惠券类型(引用CouponType里的Code)

    private Long shopId; // 优惠券适用门店 若无则为全店通用券
    @NotNull
    private TemplateRule rule; // 优惠券适用规则

    private Integer available; // 当前模版是否为可用状态
}
