package com.miyu.cloud.dms.dal.dataobject.failurerecord;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 异常记录 DO
 *
 * @author miyu
 */
@TableName("dms_failure_record")
@KeySequence("dms_failure_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailureRecordDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 设备
     */
    private String device;
    /**
     * 异常编码
     */
    private String code;
    /**
     * 故障状态
     */
    private String faultState;
    /**
     * 故障描述
     */
    private String description;
    /**
     * 故障原因
     */
    private String cause;
    /**
     * 故障时间
     */
    private LocalDateTime faultTime;
    /**
     * 维修人员
     */
    private String maintenanceBy;
    /**
     * 修复时间
     */
    private LocalDateTime repairTime;
    /**
     * 修复费用
     */
    private Double restorationCosts;
    /**
     * 备注
     */
    private String remarks;
}
