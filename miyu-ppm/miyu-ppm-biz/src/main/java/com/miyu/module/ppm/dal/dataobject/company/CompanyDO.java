package com.miyu.module.ppm.dal.dataobject.company;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 企业基本信息 DO
 *
 * @author 芋道源码
 */
@TableName("pd_company")
@KeySequence("pd_company_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 统一社会信用代码
     */
    private String usci;
    /**
     * 组织结构代码
     */
    private String organizationCode;
    /**
     * 状态
     */
    private Integer companyStatus;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 行业分类，参见国民经济行业分类
     */
    private Integer industryClassification;

    /**
     * 供求类型
     */
    private Integer supplyType;

    /**
     * 成立时间
     */
    private LocalDate formed;
    /**
     * 注册资金
     */
    private BigDecimal registrationFund;
    /**
     * 纳税人资质
     */
    private Integer taxpayer;
    /**
     * 区域
     */
    private String area;
    /**
     * 注册地址
     */
    private String registrationAddress;
    /**
     * 企业规模，几个区间
     */
    private Integer firmSize;
    /**
     * 电话
     */
    private String telephone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 公司官网
     */
    private String website;
    /**
     * 简介
     */
    private String introduction;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

}