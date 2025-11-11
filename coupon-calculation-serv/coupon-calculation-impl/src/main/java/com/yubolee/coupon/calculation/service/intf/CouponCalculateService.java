package com.yubolee.coupon.calculation.service.intf;

import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.calculation.api.beans.SimulationOrder;
import com.yubolee.coupon.calculation.api.beans.SimulationResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface CouponCalculateService {

    ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart cart);

    SimulationResponse simulateOrder(@RequestBody SimulationOrder order);
}
