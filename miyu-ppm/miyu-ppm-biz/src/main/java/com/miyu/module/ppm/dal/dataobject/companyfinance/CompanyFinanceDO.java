package com.miyu.module.ppm.dal.dataobject.companyfinance;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 企业税务信息 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_company_finance")
@KeySequence("pd_company_finance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyFinanceDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 银行
     */
    private String bank;
    /**
     * 地址
     */
    private String address;
    /**
     * 电话
     */
    private String telephone;
    /**
     * 联行号
     */
    private String bankAddress;
    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

    /**
     * 附件地址
     */
    private String fileUrl;

    /**
     * 企业名称
     */
    @TableField(exist = false)
    private String companyName;

}