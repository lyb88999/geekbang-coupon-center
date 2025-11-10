package com.yubolee.coupon.template.service;

import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import com.yubolee.coupon.template.api.beans.PagedCouponTemplateInfo;
import com.yubolee.coupon.template.api.beans.TemplateSearchParams;
import com.yubolee.coupon.template.api.enums.CouponType;
import com.yubolee.coupon.template.converter.CouponTemplateConverter;
import com.yubolee.coupon.template.dao.CouponTemplateDao;
import com.yubolee.coupon.template.dao.eneity.CouponTemplate;
import com.yubolee.coupon.template.service.intf.CouponTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    private CouponTemplateDao couponTemplateDao;

    @Override
    public CouponTemplateInfo createTemplate(CouponTemplateInfo request) {
        // 单个门店最多可以创建100张优惠券模版
        if (!Objects.isNull(request.getShopId())) {
            Integer count = couponTemplateDao.countByShopIdAndAvailable(request.getShopId(), 1);
            if (count >= 100) {
                log.error("the totals of coupon template exceeds maximum number");
                throw new UnsupportedOperationException("exceed the maximum of coupon templates that you can create");
            }
        }

        CouponTemplate template = CouponTemplate.builder()
                .name(request.getName())
                .description(request.getDesc())
                .category(CouponType.convert(request.getType()))
                .available(1)
                .shopId(request.getShopId())
                .rule(request.getRule())
                .build();
        template = couponTemplateDao.save(template);

        return CouponTemplateConverter.convertTemplateInfo(template);
    }

    @Override
    public CouponTemplateInfo loadTemplateInfo(Long id) {
        Optional<CouponTemplate> template = couponTemplateDao.findById(id);
        return template.map(CouponTemplateConverter::convertTemplateInfo).orElse(null);
    }

    @Override
    public CouponTemplateInfo cloneTemplate(Long templateId) {
        log.info("cloning template id {}", templateId);
        CouponTemplate source = couponTemplateDao.findById(templateId).orElseThrow(() -> new IllegalArgumentException("invalid template id"));
        CouponTemplate target = new CouponTemplate();
        BeanUtils.copyProperties(source, target);
        target.setAvailable(1);
        target.setId(null);

        couponTemplateDao.save(target);
        return CouponTemplateConverter.convertTemplateInfo(target);
    }

    @Override
    public PagedCouponTemplateInfo search(TemplateSearchParams request) {
        CouponTemplate example = CouponTemplate.builder()
                .shopId(request.getShopId())
                .category(CouponType.convert(request.getType()))
                .available(request.getAvailable())
                .name(request.getName())
                .build();
        Pageable page = PageRequest.of(request.getPage(), request.getPageSize());
        Page<CouponTemplate> result = couponTemplateDao.findAll(Example.of(example), page);
        List<CouponTemplateInfo> couponTemplateInfos = result.stream()
                .map(CouponTemplateConverter::convertTemplateInfo)
                .toList();

        return PagedCouponTemplateInfo.builder()
                .templates(couponTemplateInfos)
                .page(request.getPage())
                .total(result.getTotalElements())
                .build();
    }

    @Override
    public void deleteTemplate(Long id) {
        int rows = couponTemplateDao.makeCouponUnavailable(id);
        if (rows == 0) {
            throw new IllegalArgumentException("Template not found: " + id);
        }
    }

    @Override
    public Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids) {
        List<CouponTemplate> templates = couponTemplateDao.findAllById(ids);
        return templates.stream()
                .map(CouponTemplateConverter::convertTemplateInfo)
                .collect(Collectors.toMap(CouponTemplateInfo::getId, template -> template));
    }
}
