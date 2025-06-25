package com.miyu.module.dc.controller.admin.commun;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.mqtt.Config.MqttMsg;
import com.miyu.module.mqtt.Config.MyMQTTClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

@Tag(name = "mqtt通信 - 设备")
@RestController
@RequestMapping("/dc/mqtt")
@Validated
public class CommunController {

    @Resource
    private MyMQTTClient myMQTTClient;

    @Value("${spring.mqtt.topic1}")
    String topic1;
//    @Value("${spring.mqtt.topic2}")
//    String topic2;
//    @Value("${mqtt.topic3}")
//    String topic3;
//    @Value("${mqtt.topic4}")
//    String topic4;


    Queue<String> msgQueue = new LinkedList<>();
    int count = 1;

    @PostMapping("/commun")

    public void mqttMsg(@RequestBody MqttMsg mqttMsg) {
        System.out.println("队列元素数量：" + msgQueue.size());
        System.out.println("***************" + mqttMsg.getName() + ":" + mqttMsg.getContent() + "****************");
        //时间格式化
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        mqttMsg.setTime(time);

        mqttMsg.setContent(mqttMsg.getContent() + "——后台编号：" + count);
        count++;

        //map转json
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(mqttMsg));
        String sendMsg = json.toString();
        System.out.println(sendMsg);

        //队列添加元素
        boolean flag = msgQueue.offer(sendMsg);
        if (flag) {
            //发布消息  topic1 是你要发送到那个通道里面的主题 比如我要发送到topic1主题消息
            myMQTTClient.publish(msgQueue.poll(), topic1);
            System.out.println("时间戳" + System.currentTimeMillis());
        }
        System.out.println("队列元素数量：" + msgQueue.size());
    }

}
