package com.yubolee.coupon.calculation.template;

import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.calculation.template.impl.*;
import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import com.yubolee.coupon.template.api.enums.CouponType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


/**
 * 工厂方法根据订单中的优惠券信息 返回对应的Template用于计算优惠价
 */

@Slf4j
@Component
public class CouponTemplateFactory {
    @Autowired
    private MoneyOffTemplate moneyOffTemplate;

    @Autowired
    private DiscountTemplate discountTemplate;

    @Autowired
    private RandomReductionTemplate randomReductionTemplate;

    @Autowired
    private LonelyNightTemplate lonelyNightTemplate;

    @Autowired
    private DummyTemplate dummyTemplate;

    public RuleTemplate getTemplate(ShoppingCart cart) {
        if (CollectionUtils.isEmpty(cart.getCouponInfos())){
            return dummyTemplate;
        }
        // 获取优惠券的类别
        CouponTemplateInfo templateInfo = cart.getCouponInfos().get(0).getTemplate();
        CouponType category = CouponType.convert(templateInfo.getType());

        return switch (category) {
            // 订单满减券
            case MONEY_OFF -> moneyOffTemplate;
            // 订单打折券
            case DISCOUNT -> discountTemplate;
            // 随机折扣券
            case RANDOM_DISCOUNT -> randomReductionTemplate;
            // 晚间双倍优惠券
            case LONELY_NIGHT_MONEY_OFF -> lonelyNightTemplate;
            default -> dummyTemplate;
        };
    }
}
