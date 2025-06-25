package com.miyu.cloud.dms.dal.dataobject.maintainapplication;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备维修申请 DO
 *
 * @author miyu
 */
@TableName("dms_maintain_application")
@KeySequence("dms_maintain_application_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintainApplicationDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 设备id
     */
    private String device;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 生产单元编号
     */
    private String processingUnitNumber;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 关键设备
     */
    private Integer important;
    /**
     * 维修类型
     */
    private Integer type;
    /**
     * 故障信息描述
     */
    private String describe1;
    /**
     * 期望修复时间
     */
    private Integer duration;
    /**
     * 申请状态
     */
    private Integer status;
    /**
     * 流程实例编号
     */
    private String processInstanceId;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 申请时间
     */
    private LocalDateTime applicationTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
