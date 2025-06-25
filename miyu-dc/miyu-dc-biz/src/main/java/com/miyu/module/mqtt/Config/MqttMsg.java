package com.miyu.module.mqtt.Config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Data
public class MqttMsg {

    private String name = "";
    private String content = "";
    private String time = "";

    @Override
    public String toString() {
        return "MqttMsg{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

}
