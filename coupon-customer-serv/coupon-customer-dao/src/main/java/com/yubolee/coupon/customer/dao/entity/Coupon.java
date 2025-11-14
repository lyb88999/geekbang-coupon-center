package com.yubolee.coupon.customer.dao.entity;

import com.yubolee.coupon.customer.api.enums.CouponStatus;
import com.yubolee.coupon.customer.dao.converter.CouponStatusConverter;
import com.yubolee.coupon.template.api.beans.CouponTemplateInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    @Transient
    private CouponTemplateInfo templateInfo;

    @CreatedDate
    @Column(name = "created_time", nullable = false)
    private Date createdTime;
}