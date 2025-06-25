package com.miyu.cloud.macs.dal.dataobject.collector;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * (通行卡,人脸,指纹)采集器 DO
 *
 * @author 芋道源码
 */
@TableName("macs_collector")
@KeySequence("macs_collector_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectorDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 位置(0内侧,1外侧...)
     */
    private String locationCode;
    /**
     * 关联门id
     */
    private String doorId;
    /**
     * 关联设备id
     */
    private String deviceId;
    /**
     * 关联设备位置
     */
    private Integer devicePort;
    /**
     * 设备状态(0未连接,1正常,2读取,3故障...)
     */
    private Integer status;
    /**
     * 采集设备类型(1读取设备,2读写设备)
     */
    private Integer type;
    /**
     * 描述/备注
     */
    private String description;
    /**
     * 连接信息
     */
    private String connectionInformation;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createBy;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private String deviceName;
    @TableField(exist = false)
    private String doorName;
    @TableField(exist = false)
    private String regionName;

}
