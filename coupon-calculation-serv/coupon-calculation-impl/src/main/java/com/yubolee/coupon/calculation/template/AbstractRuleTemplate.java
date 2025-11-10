package com.yubolee.coupon.calculation.template;

import com.yubolee.coupon.calculation.api.beans.Product;
import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractRuleTemplate implements RuleTemplate{
    @Override
    public ShoppingCart calculate(ShoppingCart settlement) {
        // 获取订单总价
        Long orderTotalAmount = getTotalPrice(settlement.getProducts());
        // 获取以shopId为维度的价格统计
        Map<Long, Long> sumAmount = this.getTotalPriceGroupByShop(settlement.getProducts());

        // 以下规则只做单个优惠券的计算
        CouponTemplateInfo template = settlement.getCouponInfos().get(0).getTemplate();
        // 最低消费限制
        Long threshold = template.getRule().getDiscount().getThreshold();
        // 优惠金额或打折比例
        Long quota = template.getRule().getDiscount().getQuota();
        // 当前优惠券适用的门店ID
        Long shopId = template.getShopId();

        Long shopTotalAmount = Objects.isNull(shopId) ? orderTotalAmount : sumAmount.get(shopId);

        // 如果不符合优惠券使用标准则直接按原价走 不使用消费券
        if(Objects.isNull(shopTotalAmount) || shopTotalAmount < threshold){
            log.warn("Totals of amount not meet, ur coupons are not applicable to this order");
            settlement.setCost(orderTotalAmount);
            settlement.setCouponInfos(Collections.emptyList());
            return settlement;
        }
        // 子类中计算新的价格
        Long newCost = calculateNewPrice(orderTotalAmount, shopTotalAmount, quota);
        // 订单价格不能小于最低价格
        if (newCost < minCost()){
            newCost = minCost();
        }
        settlement.setCost(newCost);
        log.debug("original price={}, new price={}", orderTotalAmount, newCost);
        return settlement;

    }

    // 金额计算具体逻辑，延迟到子类实现
    abstract protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota);


    // 根据门店维度计算每个门店下商品价格
    // key = shopId
    // value = 门店商品总价
    private Map<Long, Long> getTotalPriceGroupByShop(@NotEmpty List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(m -> m.getShopId(),
                        Collectors.summingLong(p -> p.getPrice() * p.getCount())));
    }

    // 计算订单总价
    private Long getTotalPrice(@NotEmpty List<Product> products) {
        return products.stream()
                .mapToLong(product -> product.getPrice() * product.getCount())
                .sum();
    }

    // 每个订单最少必须支付1分钱
    protected long minCost() {
        return 1L;
    }
}
