package com.zipking.cloud.springbootmybatis.rule.service.impl;

import com.zipking.cloud.springbootmybatis.rule.model.RuleDto;
import com.zipking.cloud.springbootmybatis.rule.service.BaseRule;

public abstract class AbstractRule implements BaseRule {

    protected <T> T convert(RuleDto dto) {
        return (T) dto;
    }

    @Override
    public boolean execute(RuleDto dto) {
        return executeRule(convert(dto));
    }

    protected <T> boolean executeRule(T t) {
        return true;
    }
}
