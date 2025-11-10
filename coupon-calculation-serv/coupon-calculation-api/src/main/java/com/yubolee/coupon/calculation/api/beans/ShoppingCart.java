package com.yubolee.coupon.calculation.api.beans;


import com.yubolee.coupon.template.api.beans.CouponInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    @NotEmpty
    private List<Product> products;

    private Long couponId;

    private List<CouponInfo> couponInfos;

    private Long cost;

    @NotNull
    private Long userId;
}
