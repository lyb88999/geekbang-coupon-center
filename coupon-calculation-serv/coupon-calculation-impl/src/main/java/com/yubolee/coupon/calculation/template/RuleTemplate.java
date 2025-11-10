package com.yubolee.coupon.calculation.template;

import com.yubolee.coupon.calculation.api.beans.ShoppingCart;

public interface RuleTemplate {

    ShoppingCart calculate(ShoppingCart settlement);
}
