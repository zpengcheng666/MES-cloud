package com.miyu.cloud.macs.dal.dataobject.deviceConfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("macs_device_config")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConfigDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String deviceId;

    private String users;

    private String visitors;
}
