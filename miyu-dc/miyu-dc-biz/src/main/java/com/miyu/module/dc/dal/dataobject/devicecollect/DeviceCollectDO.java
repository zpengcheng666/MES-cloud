package com.miyu.module.dc.dal.dataobject.devicecollect;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.math.BigDecimal;

@TableName("dc_device_collect")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCollectDO extends BaseDO {

    /**
     * 采集类型主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *设备id
     */
    private String deviceId;

    /**
     * 产品类型id
     */
    private String productTypeId;

    /**
     * topicId(通信Id)
     */
    private String topicId;

    /**
     * 在线状态(1.在线 2.异常 0.未接入平台)
     */
    private Integer onlineStatus;

    /**
     * 标准值状态(1.在线 2.异常 0.未接入平台)
     */
    private Integer normStatus;

    /**
     *
     */
    @TableField(exist = false)
    private BigDecimal collectAttributesCycle;

    @TableField(exist = false)
    private String productTopicId;
}
