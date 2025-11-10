package com.yubolee.coupon.calculation.template.impl;

import com.yubolee.coupon.calculation.template.AbstractRuleTemplate;

public class MoneyOffTemplate extends AbstractRuleTemplate {
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        return 0L;
    }
}
