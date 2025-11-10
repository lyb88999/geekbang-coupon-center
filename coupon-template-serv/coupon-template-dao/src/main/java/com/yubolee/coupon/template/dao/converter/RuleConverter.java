package com.yubolee.coupon.template.dao.converter;

import com.alibaba.fastjson.JSON;
import com.yubolee.coupon.template.api.beans.rules.TemplateRule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RuleConverter implements AttributeConverter<TemplateRule, String> {
    @Override
    public String convertToDatabaseColumn(TemplateRule templateRule) {
        return JSON.toJSONString(templateRule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String s) {
        return JSON.parseObject(s, TemplateRule.class);
    }
}
