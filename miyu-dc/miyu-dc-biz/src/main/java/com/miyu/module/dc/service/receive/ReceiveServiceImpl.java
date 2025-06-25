package com.miyu.module.dc.service.receive;

import cn.hutool.json.JSONObject;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import com.miyu.module.dc.dal.dataobject.mqtt.ReceiveDO;
import com.miyu.module.dc.dal.mysql.device.DeviceMapper;
import com.miyu.module.dc.service.device.DeviceService;
import com.miyu.module.dc.service.device.DeviceServiceImpl;
import com.miyu.module.dc.service.devicedate.DeviceDateServiceImpl;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Validated
public class ReceiveServiceImpl implements ReceiveService {

    @Resource
    DeviceServiceImpl deviceServiceImpl;

    @Resource
    DeviceMapper deviceMapper;

    @Resource
    DeviceDateServiceImpl DeviceDateServiceImpl;

    private static final Logger log = LoggerFactory.getLogger(ReceiveServiceImpl.class);

    @Override
    public void ReceiveHandle(String topic, String code) {
        DeviceDateServiceImpl.insertDeviceDate(topic,code);
    }



}
