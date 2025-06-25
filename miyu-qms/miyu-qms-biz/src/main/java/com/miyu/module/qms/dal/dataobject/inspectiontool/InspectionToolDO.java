package com.miyu.module.qms.dal.dataobject.inspectiontool;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检测工具 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_tool")
@KeySequence("qms_inspection_tool_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionToolDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 检测工具名称
     */
    private String name;

    /**
     * 规格
     */
    private String spec;

    /**
     * 测量范围
     */
    private String measuringRange;

    /**
     * 准确等级
     */
    private String accuracyLevel;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 出厂编号
     */
    private String manufacturerNumber;

    /**
     * 本厂编号
     */
    private String barCode;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 检/校准日期
     */
    private LocalDateTime verificationDate;

    /**
     * 检/校定周期
     */
    private Integer verificationCycle;

    /**
     * 库存主键
     */
    private String stockId;

    /**
     * 物料类型主键
     */
    private String materialConfigId;
}