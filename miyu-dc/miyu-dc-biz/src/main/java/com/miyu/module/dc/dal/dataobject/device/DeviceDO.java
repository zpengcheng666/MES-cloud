package com.miyu.module.dc.dal.dataobject.device;

import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备 DO
 *
 * @author 芋道源码
 */
@TableName("dc_device")
@KeySequence("dc_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDO extends BaseDO {

    /**
     * 设备id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 设备名称
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private String deviceTypeId;

    /**
     * 通信类型
     */
    private Integer commType;

    /**
     * mqtt客户端id
     */
    private String deviceClientId;

    /**
     * 通讯url
     */
    private String deviceUrl;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @TableField(exist = false)
    private String[] productTypeId;

    @TableField(exist = false)
    private String DeviceJson;

    @TableField(exist = false)
    private List<ProductTypeDO> productTypeDOS;

}