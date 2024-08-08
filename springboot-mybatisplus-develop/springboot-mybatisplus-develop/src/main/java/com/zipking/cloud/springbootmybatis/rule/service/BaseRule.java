package com.zipking.cloud.springbootmybatis.rule.service;

import com.zipking.cloud.springbootmybatis.rule.model.RuleDto;


/**
 * 优点：
 * 比较简单，每个规则可以独立，将规则，数据，执行器拆分出来，调用方比较规整；
 * 在 Rule 模板类中定义 convert 方法做参数的转换这样可以能够，为特定 rule 需要的场景数据提供拓展。
 * 比较简单，每个规则可以独立，将规则，数据，执行器拆分出来，调用方比较规整
 * 缺点：
 * 上下 rule 有数据依赖性，如果直接修改公共传输对象 dto 这样设计不是很合理，建议提前构建数据。
 * 数据依赖公共传输对象 dto
 *
 * 自己总结：
 * 比较适合规范性、平台性较强的的实现。方便bug统一调节、拦截、处理等。
 * 对于业务较强的判断。可以直接选择if-else。
 * */
public interface BaseRule {
    boolean execute(RuleDto dto);
}
