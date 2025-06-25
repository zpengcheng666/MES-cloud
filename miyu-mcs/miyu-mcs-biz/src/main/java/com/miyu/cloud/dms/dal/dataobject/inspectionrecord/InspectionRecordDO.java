package com.miyu.cloud.dms.dal.dataobject.inspectionrecord;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备检查记录 DO
 *
 * @author 王正浩
 */
@TableName("dms_inspection_record")
@KeySequence("dms_inspection_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionRecordDO extends BaseDO {

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
     * 记录状态
     */
    private Integer status;
    /**
     * 设备
     */
    private String device;
    /**
     * 是否超期停机
     */
    private Integer expirationShutdown;
    /**
     * 超期时间
     */
    private Integer expirationTime;
    /**
     * 检查类型
     * <p>
     * 枚举 {@link TODO dms_inspection_type 对应的类}
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;
    /**
     * 检查内容
     */
    private String content;
    /**
     * 检查人
     */
    private String createBy;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}