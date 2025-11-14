package com.yubolee.coupon.calculation.controller;

import com.alibaba.fastjson.JSON;
import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.calculation.api.beans.SimulationOrder;
import com.yubolee.coupon.calculation.api.beans.SimulationResponse;
import com.yubolee.coupon.calculation.service.intf.CouponCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/calculator")
public class CouponCalculationController {

    @Autowired
    private CouponCalculateService couponCalculateService;

    // 优惠券结算
    @PostMapping("/checkout")
    @ResponseBody
    public ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart settlement) {
        log.info("do calculation: {}", JSON.toJSONString(settlement));
        return couponCalculateService.calculateOrderPrice(settlement);
    }

    // 优惠券列表挨个试算
    // 给客户提示每个可用优惠券的优惠额度 帮助挑选
    @PostMapping("/simulate")
    @ResponseBody
    public SimulationResponse simulate(@RequestBody SimulationOrder order) {
        log.info("do simulation: {}", JSON.toJSONString(order));
        return couponCalculateService.simulateOrder(order);
    }
}
