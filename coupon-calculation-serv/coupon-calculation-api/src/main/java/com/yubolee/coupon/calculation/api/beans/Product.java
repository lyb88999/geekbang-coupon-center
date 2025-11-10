package com.yubolee.coupon.calculation.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long productId;

    // 商品价格
    private Long price;

    // 商品在购物车的数量
    private Integer count;

    // 商品销售的门店
    private Long shopId;
}
