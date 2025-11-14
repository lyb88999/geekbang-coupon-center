package com.yubolee.coupon.customer.service.intf;

import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.calculation.api.beans.SimulationOrder;
import com.yubolee.coupon.calculation.api.beans.SimulationResponse;
import com.yubolee.coupon.customer.api.beans.RequestCoupon;
import com.yubolee.coupon.customer.api.beans.SearchCoupon;
import com.yubolee.coupon.customer.dao.entity.Coupon;
import com.yubolee.coupon.template.api.beans.CouponInfo;

import java.util.List;

public interface CouponCustomerService {

    // 用户领取优惠券
    Coupon requestCoupon(RequestCoupon request);

    // 用户核销优惠券
    ShoppingCart placeOrder(ShoppingCart info);

    // 优惠券金额试算
    SimulationResponse simulateOrderPrice(SimulationOrder order);

    void deleteCoupon(Long userId, Long couponId);

    // 查询用户优惠券
    List<CouponInfo> findCoupon(SearchCoupon request);
}
