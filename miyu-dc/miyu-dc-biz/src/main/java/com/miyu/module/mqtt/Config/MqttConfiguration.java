package com.miyu.module.mqtt.Config;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 1. @author yuhao
 */
@Component
@Configuration
@Data
public class MqttConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MqttConfiguration.class);
    @Value("${spring.mqtt.host}")
    String host;
    @Value("${spring.mqtt.username}")
    String username;
    @Value("${spring.mqtt.password}")
    String password;
    @Value("${spring.mqtt.clientId}")
    String clientId;
    @Value("${spring.mqtt.timeout}")
    int timeOut;
    @Value("${spring.mqtt.keepalive}")
    int keepAlive;
    @Value("${spring.mqtt.qos}")
    String qos;
    @Value("${spring.mqtt.topic1}")
    String topic1;
//    @Value("${spring.mqtt.topic2}")
//    String topic2;
//    @Value("${spring.mqtt.topic3}")
//    String topic3;
//    @Value("${spring.mqtt.topic4}")
//    String topic4;

    @Bean//注入spring
    public MyMQTTClient myMQTTClient() {
        MyMQTTClient myMQTTClient = new MyMQTTClient(host, username, password, clientId, timeOut, keepAlive);
        for (int i = 0; i < 10; i++) {
            try {
                myMQTTClient.connect();
                //不同的主题
                   myMQTTClient.subscribe(topic1, 1);
//                   myMQTTClient.subscribe(topic2, 1);
//                   myMQTTClient.subscribe(topic3, 1);
//                   myMQTTClient.subscribe(topic4, 1);
                return myMQTTClient;
            } catch (MqttException e) {
                log.error("MQTT connect exception,connect time = " + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return myMQTTClient;
    }

}
