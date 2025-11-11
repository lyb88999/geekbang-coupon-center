package com.yubolee.coupon.calculation.template.impl;

import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.calculation.template.AbstractRuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DummyTemplate extends AbstractRuleTemplate {

    @Override
    public ShoppingCart calculate(ShoppingCart settlement) {
        Long orderTotalAmount = getTotalPrice(settlement.getProducts());
        settlement.setCost(orderTotalAmount);
        return settlement;
    }

    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        return orderTotalAmount;
    }
}
