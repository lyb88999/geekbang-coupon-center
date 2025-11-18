package com.yubolee.coupon.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.yubolee.coupon.customer.service",
        "com.yubolee.coupon.customer.controller",
        "com.yubolee.coupon.template.dao",
        "com.yubolee.coupon.customer.config",
        "com.yubolee.coupon.customer.feign",
        "com.yubolee.coupon.customer.interceptor"}) // 添加这一行
@EnableJpaRepositories(basePackages = {"com.yubolee.coupon.customer.dao",
        "com.yubolee.coupon.template.dao"})
@EntityScan(basePackages = {"com.yubolee.coupon.template.dao.eneity",
        "com.yubolee.coupon.customer.dao.entity"})
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableFeignClients
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}