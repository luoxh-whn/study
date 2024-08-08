package com.zipking.cloud.springbootmybatis.rule;

import com.zipking.cloud.springbootmybatis.rule.model.RuleDto;
import com.zipking.cloud.springbootmybatis.rule.service.impl.AddressRule;
import com.zipking.cloud.springbootmybatis.rule.service.impl.NationalityRule;
import com.zipking.cloud.springbootmybatis.rule.service.impl.RuleService;
import org.junit.Test;

import java.util.Arrays;

public class RuleServiceTest {

    @Test
    public void execute() {
        //规则执行器
        //优点：比较简单，每个规则可以独立，将规则，数据，执行器拆分出来，调用方比较规整
        //缺点：数据依赖公共传输对象 dto

        //1. 定义规则  init rule
        NationalityRule nationalityRule = new NationalityRule();
        AddressRule addressRule = new AddressRule();

        //2. 构造需要的数据 create dto
        RuleDto dto = new RuleDto();
        dto.setAge(5);
        dto.setAddress("北京");

        //3. 通过以链式调用构建和执行 rule execute
        boolean ruleResult = RuleService
                .create()
                .and(Arrays.asList(nationalityRule, addressRule))
                .execute(dto);
        System.out.println("this student rule execute result :" + ruleResult);
    }
}
