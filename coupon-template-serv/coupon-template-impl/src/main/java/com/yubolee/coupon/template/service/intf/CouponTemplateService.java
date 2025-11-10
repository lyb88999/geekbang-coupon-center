package com.yubolee.coupon.template.service.intf;

import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import com.yubolee.coupon.template.api.beans.PagedCouponTemplateInfo;
import com.yubolee.coupon.template.api.beans.TemplateSearchParams;

import java.util.Collection;
import java.util.Map;

public interface CouponTemplateService {

    // 创建优惠券模版
    CouponTemplateInfo createTemplate(CouponTemplateInfo request);

    // 通过模版ID查询优惠券模版
    CouponTemplateInfo loadTemplateInfo(Long id);

    // 克隆券模版
    CouponTemplateInfo cloneTemplate(Long templateId);

    // 模版查询(分页)
    PagedCouponTemplateInfo search(TemplateSearchParams request);

    // 删除券模版
    void deleteTemplate(Long id);

    // 批量读取模版
    Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids);
}
