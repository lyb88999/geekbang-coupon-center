package com.yubolee.coupon.calculation.template.impl;

import com.yubolee.coupon.calculation.template.AbstractRuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 满减优惠券计算规则
 */
@Slf4j
@Component
public class MoneyOffTemplate extends AbstractRuleTemplate {
    /**
     * 满减优惠券计算规则
     *
     * @param orderTotalAmount 订单总价
     * @param shopTotalAmount  门店商品总价
     * @param quota            优惠券金额
     * @return 新的订单金额
     */
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        // 满减优惠券金额 = 订单金额 >= 优惠券金额 ? 优惠券金额 : 订单金额
        Long benefitAmount = shopTotalAmount < quota ? shopTotalAmount : quota;
        log.debug("original price={}, new price={}", orderTotalAmount, orderTotalAmount - benefitAmount);
        return orderTotalAmount - benefitAmount;
    }
}
