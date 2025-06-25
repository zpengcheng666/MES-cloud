package com.miyu.cloud.macs.service.collectorConfig;

import com.miyu.cloud.macs.dal.mysql.deviceConfig.DeviceConfigMapper;

import javax.annotation.Resource;

public class DoorConfigServiceImpl implements DoorConfigService {

    @Resource
    private DeviceConfigMapper deviceConfigMapper;
}
