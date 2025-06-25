package com.miyu.cloud.macs.dal.dataobject.door;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 门 DO
 *
 * @author 芋道源码
 */
@TableName("macs_door")
@KeySequence("macs_door_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoorDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 关联区域id
     */
    private String regionId;
    /**
     * 关联设备id
     */
    private String deviceId;
    /**
     * 位置
     */
    private String locationName;
    /**
     * 门禁状态(0关闭,1打开,2故障)
     */
    private Integer doorStatus;
    /**
     * 描述/备注
     */
    private String description;
    /**
     * 关联设备位置
     */
    private Integer devicePort;

    @TableField(exist = false)
    private String regionName;
    @TableField(exist = false)
    private String deviceName;

}