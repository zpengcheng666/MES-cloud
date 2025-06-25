package cn.iocoder.yudao.module.pms.dal.dataobject.plan;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目计划子表，物料采购计划中的设备采购 DO
 *
 * @author 上海弥彧
 */
@TableName("project_plan_device")
@KeySequence("project_plan_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDeviceDO extends BaseDO {

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
     * 项目计划子计划id
     */
    private String projectPlanItemId;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 零部件版本id
     */
    private String partVersionId;
    /**
     * 图号
     */
    private String partNumber;
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
     * 设备类型(对应dms_ledger的equipment_station_type)
     */
    private String type;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 规格
     */
    private String specification;
    /**
     * 工序名(弃用)
     */
    private String procedureName;
    /**
     * 预估单价
     */
    private BigDecimal predictPrice;

}
