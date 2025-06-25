package com.miyu.module.ppm.dal.dataobject.companyproduct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 企业产品表，用于销售和采购 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_company_product")
@KeySequence("pd_company_product_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProductDO extends BaseDO {

    /**
     * 编码
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 企业ID
     */
    private String companyId;
    /**
     * material表ID
     */
    private String materialId;
    /**
     * 初始单价
     */
    private BigDecimal initPrice;

    /**
     * 初始税率
     */
    private String initTax;

    /**
     * 供货周期
     */
    private Integer leadTime;

    /**
     * 是否免检
     */
    private Integer qualityCheck;
    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

    /**
     * 企业名称
     */
    @TableField(exist = false)
    private String companyName;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

}