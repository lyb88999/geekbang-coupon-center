package com.yubolee.coupon.calculation.template.impl;

import com.yubolee.coupon.calculation.template.AbstractRuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 打折优惠券计算规则
 */

@Slf4j
@Component
public class DiscountTemplate extends AbstractRuleTemplate {
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        Long newPrice = convertToDecimal(shopTotalAmount * (quota.doubleValue() / 100));
        log.debug("original price={}, new price={}", orderTotalAmount, newPrice);
        return newPrice;
    }
}
