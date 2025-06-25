package com.miyu.module.ppm.dal.dataobject.companyqualitycontrol;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 企业质量控制信息 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_company_quality_control")
@KeySequence("pd_company_quality_control_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyQualityControlDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 企业编号
     */
    private String companyId;
    /**
     * 质量管理体系认证
     */
    private Integer qmsc;
    /**
     * 是否专职检验
     */
    private Integer inspection;
    /**
     * 是否不合格品控制
     */
    private Integer  nonconformingControl;
    /**
     * 生产可追溯
     */
    private Integer productionTraceability;
    /**
     * 是否采购质量控制
     */
    private Integer purchasingControl;
    /**
     * 出厂质量控制
     */
    private Integer oqc;
    /**
     * 备注
     */
    private String remark;
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

}