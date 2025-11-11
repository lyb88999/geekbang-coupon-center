package com.yubolee.coupon.calculation.template.impl;

import com.yubolee.coupon.calculation.template.AbstractRuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 随机减免优惠券计算规则
 */

@Slf4j
@Component
public class RandomReductionTemplate extends AbstractRuleTemplate {
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        long maxBenefit = Math.min(shopTotalAmount, quota);
        int reductionAmount = new Random().nextInt((int) maxBenefit);
        Long newCost = orderTotalAmount - reductionAmount;
        log.debug("original price={}, new price={}", orderTotalAmount, newCost);
        return newCost;
    }
}
