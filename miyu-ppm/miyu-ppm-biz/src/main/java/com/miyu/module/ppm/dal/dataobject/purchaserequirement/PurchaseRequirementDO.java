package com.miyu.module.ppm.dal.dataobject.purchaserequirement;

import lombok.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 采购申请主 DO
 *
 * @author Zhangyunfei
 */
@TableName("ppm_purchase_requirement")
@KeySequence("ppm_purchase_requirement_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequirementDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 采购类型
     */
    private Integer type;

    /**
     * 采购单号
     */
    private String number;

    /**
     * 申请人
     */
    private String applicant;
    /**
     * 申请部门
     */
    private String applicationDepartment;
    /**
     * 申请日期
     */
    private LocalDateTime applicationDate;
    /**
     * 申请理由
     */
    private String applicationReason;

    /**
     * 是否有效
     */
    private Integer isValid;

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