package com.miyu.module.dc.service.receive;

import com.miyu.module.dc.dal.dataobject.mqtt.ReceiveDO;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

public interface ReceiveService {


    /**
     * 处理mqtt接收数据
     * @param topic
     * @param code
     */
    void ReceiveHandle(String topic, String code);

}
