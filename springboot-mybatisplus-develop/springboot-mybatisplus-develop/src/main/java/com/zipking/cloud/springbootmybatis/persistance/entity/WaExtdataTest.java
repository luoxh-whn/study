package com.zipking.cloud.springbootmybatis.persistance.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 外部数据明细测试表
 * </p>
 *
 * @author zhaokun
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wa_extdata_test")
public class WaExtdataTest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 所属组织
     */
    private String busiorg;

    /**
     * 所属组织名称
     */
    private String busiorgName;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 人员编码
     */
    private String staffCode;

    /**
     * 人员名称
     */
    private String staffName;

    /**
     * 期间
     */
    private String period;

    /**
     * 期间类型
     */
    private String periodType;

    private BigDecimal jjgz;

    private BigDecimal jjdj;

    private String code1;

    private String code2;

    private Boolean bbb1;

    private LocalDateTime ddd1;

    private LocalDateTime ddd2;


}
