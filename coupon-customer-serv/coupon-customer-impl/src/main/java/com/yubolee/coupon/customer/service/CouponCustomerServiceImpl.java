package com.yubolee.coupon.customer.service;

import com.google.common.collect.Lists;
import com.yubolee.coupon.calculation.api.beans.ShoppingCart;
import com.yubolee.coupon.calculation.api.beans.SimulationOrder;
import com.yubolee.coupon.calculation.api.beans.SimulationResponse;
import com.yubolee.coupon.customer.api.beans.RequestCoupon;
import com.yubolee.coupon.customer.api.beans.SearchCoupon;
import com.yubolee.coupon.customer.api.enums.CouponStatus;
import com.yubolee.coupon.customer.converter.CouponConverter;
import com.yubolee.coupon.customer.dao.CouponDao;
import com.yubolee.coupon.customer.dao.entity.Coupon;
import com.yubolee.coupon.customer.feign.CalculationService;
import com.yubolee.coupon.customer.feign.TemplateService;
import com.yubolee.coupon.customer.service.intf.CouponCustomerService;
import com.yubolee.coupon.template.api.beans.CouponInfo;
import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CouponCustomerServiceImpl implements CouponCustomerService {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private CalculationService calculationService;

    @Override
    public Coupon requestCoupon(RequestCoupon request) {
        CouponTemplateInfo templateInfo = templateService.getTemplate(request.getCouponTemplateId());

        // 找不到优惠券模版报错
        if (Objects.isNull(templateInfo)) {
            log.error("invalid template id={} ", request.getCouponTemplateId());
            throw new IllegalArgumentException("Invalid template_id");
        }

        // 模版不能过期
        long now = Calendar.getInstance().getTimeInMillis();
        Long expTime = templateInfo.getRule().getDeadline();
        if (!Objects.isNull(expTime) && now > expTime || Objects.equals(templateInfo.getAvailable(), 0)) {
            log.error("template is not available id={}", request.getCouponTemplateId());
            throw new IllegalArgumentException("template is unavailable");
        }

        // 用户领券数量到达上限
        long count = couponDao.countByUserIdAndTemplateId(request.getUserId(), request.getCouponTemplateId());
        if (count >= templateInfo.getRule().getLimitation()) {
            log.error("exceeds maximum number");
            throw new IllegalArgumentException("exceeds maximum number");
        }

        Coupon coupon = new Coupon();
        coupon.setTemplateId(request.getCouponTemplateId());
        coupon.setUserId(request.getUserId());
        coupon.setShopId(templateInfo.getShopId());
        coupon.setStatus(CouponStatus.AVAILABLE);
        couponDao.save(coupon);
        return coupon;
    }

    @Override
    public ShoppingCart placeOrder(ShoppingCart order) {
        if (CollectionUtils.isEmpty(order.getProducts())) {
            log.error("invalid check out request, order={}", order);
            throw new IllegalArgumentException("cart is empty");
        }

        Coupon coupon = null;
        if (!Objects.isNull(order.getCouponId())) {
            // 如果有优惠券 验证是否可用 并且是当前客户的
            Coupon example = Coupon.builder()
                    .userId(order.getUserId())
                    .id(order.getCouponId())
                    .status(CouponStatus.AVAILABLE)
                    .build();
            coupon = couponDao.findAll(Example.of(example))
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("coupon not found"));
            CouponInfo couponInfo = CouponConverter.convertToCoupon(coupon);
            CouponTemplateInfo templateInfo = templateService.getTemplate(coupon.getTemplateId());
            couponInfo.setTemplate(templateInfo);
            order.setCouponInfos(Lists.newArrayList(couponInfo));
        }
        ShoppingCart checkoutInfo = calculationService.checkout(order);

        if (!Objects.isNull(coupon)) {
            if (CollectionUtils.isEmpty(checkoutInfo.getCouponInfos())) {
                log.error("cannot apply coupon to order, couponId={}", coupon.getId());
                throw new IllegalArgumentException("coupon is not applicable to this order");
            }
            log.info("update coupon status to used, couponId={}", coupon.getId());
            coupon.setStatus(CouponStatus.USED);
            couponDao.save(coupon);
        }
        return checkoutInfo;
    }

    @Override
    public SimulationResponse simulateOrderPrice(SimulationOrder order) {
        List<CouponInfo> couponInfoList = Lists.newArrayList();
        for (Long couponId : order.getCouponIds()) {
            Coupon example = Coupon.builder()
                    .userId(order.getUserId())
                    .id(couponId)
                    .status(CouponStatus.AVAILABLE)
                    .build();
            Optional<Coupon> couponOptional = couponDao.findAll(Example.of(example))
                    .stream()
                    .findFirst();
            if (couponOptional.isPresent()) {
                Coupon coupon = couponOptional.get();
                CouponInfo couponInfo = CouponConverter.convertToCoupon(coupon);
                CouponTemplateInfo templateInfo = templateService.getTemplate(coupon.getTemplateId());
                couponInfo.setTemplate(templateInfo);
                couponInfoList.add(couponInfo);
            }
        }
        order.setCouponInfos(couponInfoList);
        return calculationService.simulate(order);
    }

    @Override
    public void deleteCoupon(Long userId, Long couponId) {
        Coupon example = Coupon.builder()
                .userId(userId)
                .id(couponId)
                .status(CouponStatus.AVAILABLE)
                .build();
        Coupon coupon = couponDao.findAll(Example.of(example))
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find available coupon"));
        coupon.setStatus(CouponStatus.INACTIVE);
        couponDao.save(coupon);
    }

    @Override
    public List<CouponInfo> findCoupon(SearchCoupon request) {
        Coupon coupon = Coupon.builder()
                .userId(request.getUserId())
                .status(CouponStatus.convert(request.getCouponStatus()))
                .shopId(request.getShopId())
                .build();

        List<Coupon> coupons = couponDao.findAll(Example.of(coupon));
        if (coupons.isEmpty()) {
            return Lists.newArrayList();
        }
        List<Long> templateIds = coupons.stream()
                .map(Coupon::getTemplateId)
                .distinct()
                .toList();
        Map<Long, CouponTemplateInfo> templateInfoMap = templateService.getTemplateInBatch(templateIds);
        coupons.stream().forEach(e -> e.setTemplateInfo(templateInfoMap.get(e.getTemplateId())));
        return coupons.stream()
                .map(CouponConverter::convertToCoupon)
                .toList();
    }
}
