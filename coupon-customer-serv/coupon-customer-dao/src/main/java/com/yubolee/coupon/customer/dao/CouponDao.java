package com.yubolee.coupon.customer.dao;

import com.yubolee.coupon.customer.dao.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponDao extends JpaRepository<Coupon, Long> {
    // 根据用户id和优惠券模版id统计用户从当前优惠券模版中领了多少张券
    long countByUserIdAndTemplateId(Long userId, Long templateId);
}
