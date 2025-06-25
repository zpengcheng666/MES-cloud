package com.miyu.cloud.macs.dal.dataobject.device;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * 设备 DO
 *
 * @author 芋道源码
 */
@TableName("macs_device")
@KeySequence("macs_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态(0未连接,1正常,2故障...)
     */
    private Integer status;
    /**
     * 区域id
     */
    private String regionId;
    /**
     * 区域名称
     */
    @TableField(exist = false)
    private String regionName;

    /**
     * ip
     */
    private String ip;
    /**
     * 端口号
     */
    private String port;
    /**
     * 账户
     */
    private String accountNumber;
    /**
     * 密码
     */
    private String password;

    private String manufacturer;
    private String unit;
    private String connectionInformation;
    /**
     * 启用状态(1启用,0禁用)
     */
    private Integer enableStatus;
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
}
