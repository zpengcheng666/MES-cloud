package cn.iocoder.yudao.module.pms.dal.dataobject.plan;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目计划子表，物料采购计划中的设备采购 DO
 *
 * @author 上海弥彧
 */
@TableName("project_plan_demand_hilt")
@KeySequence("project_plan_demand_hilt_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDemandHiltDO extends BaseDO {

    /**
     * 项目计划id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目计划id
     */
    private String projectPlanId;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 采购数量
     */
    private Integer purchaseAmount;
    /**
     * 制造资源类型：1设备 2刀具 3工装
     */
    private Integer resourcesType;
    /**
     * 制造资源id(设备、刀具、工装等)
     */
    private String resourcesTypeId;
    /**
     * 零部件版本id
     */
    private String partVersionId;
    /**
     * 接口型式id
     */
    private String taperTypeId;
    /**
     * 刀柄长度
     */
    private String length;
    /**
     * 刀柄前端直径
     */
    private String frontEndDiameter;
    /**
     * 刀柄前端长度
     */
    private String frontEndLength;
    /**
     * 倾角
     */
    private String dipAngle;
    /**
     * 备注
     */
    private String description;
    /**
     * 采购标记(0为未发起采购,1为已采购)
     */
    private Integer purchaseMark;
    /**
     * 采购时间
     */
    private LocalDateTime purchaseTime;
    /**
     * 预估单价
     */
    private BigDecimal predictPrice;

}
