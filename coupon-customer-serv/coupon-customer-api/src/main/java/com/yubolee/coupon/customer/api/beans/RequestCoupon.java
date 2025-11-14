package com.yubolee.coupon.customer.api.beans;

import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCoupon {

    @NotNull
    private Long userId;

    @NotNull
    private Long couponTemplateId;

    private CouponTemplateInfo templateSDK;

    private String trafficVersion;
}
