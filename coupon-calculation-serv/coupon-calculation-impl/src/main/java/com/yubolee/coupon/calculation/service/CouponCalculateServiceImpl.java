package com.yubolee.coupon.calculation.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.calculation.api.beans.SimulationOrder;
import com.yubolee.coupon.calculation.api.beans.SimulationResponse;
import com.yubolee.coupon.calculation.service.intf.CouponCalculateService;
import com.yubolee.coupon.calculation.template.CouponTemplateFactory;
import com.yubolee.coupon.calculation.template.RuleTemplate;
import com.yubolee.coupon.template.api.beans.CouponInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CouponCalculateServiceImpl implements CouponCalculateService {

    @Autowired
    private CouponTemplateFactory couponTemplateFactory;


    // 优惠券结算
    // 这里通过Factory类决定使用哪个底层Rule，底层规则对上层透明
    @Override
    public ShoppingCart calculateOrderPrice(ShoppingCart cart) {
        log.info("calculate order price: {}", JSON.toJSONString(cart));
        RuleTemplate ruleTemplate = couponTemplateFactory.getTemplate(cart);
        return ruleTemplate.calculate(cart);
    }

    @Override
    public SimulationResponse simulateOrder(SimulationOrder order) {
        SimulationResponse response = new SimulationResponse();
        long minOrderPrice = Long.MAX_VALUE;

        for (CouponInfo coupon : order.getCouponInfos()) {
            ShoppingCart cart = new ShoppingCart();
            cart.setProducts(order.getProducts());
            cart.setCouponInfos(Lists.newArrayList(coupon));
            cart = couponTemplateFactory.getTemplate(cart).calculate(cart);

            Long couponId = coupon.getId();
            Long orderPrice = cart.getCost();

            response.getCouponToOrderPrice().put(couponId, orderPrice);

            if (minOrderPrice > orderPrice) {
                response.setBestCouponId(coupon.getId());
                minOrderPrice = orderPrice;
            }
        }
        return response;
    }
}
