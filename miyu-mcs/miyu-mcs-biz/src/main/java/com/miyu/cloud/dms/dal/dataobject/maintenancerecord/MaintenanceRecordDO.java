package com.miyu.cloud.dms.dal.dataobject.maintenancerecord;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备保养维护记录 DO
 *
 * @author miyu
 */
@TableName("dms_maintenance_record")
@KeySequence("dms_maintenance_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecordDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 计划编码
     */
    private String code;
    /**
     * 保养维护记录状态
     */
    private Integer recordStatus;
    /**
     * 设备
     */
    private String device;
    /**
     * 是否为关键设备
     */
    private Integer criticalDevice;
    /**
     * 是否超期停机
     */
    private Integer expirationShutdown;
    /**
     * 超期时间
     */
    private Integer expirationTime;
    /**
     * 保养类型
     */
    private Integer type;
    /**
     * 完成状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 保养内容
     */
    private String content;
    /**
     * 保养人
     */
    private String maintenanceBy;
    /**
     * 开始维护时间
     */
    private LocalDateTime startTime;
    /**
     * 结束维护时间
     */
    private LocalDateTime endTime;

}
