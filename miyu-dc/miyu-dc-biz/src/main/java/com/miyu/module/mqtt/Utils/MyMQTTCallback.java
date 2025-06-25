package com.miyu.module.mqtt.Utils;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.miyu.module.dc.dal.dataobject.mqtt.ReceiveDO;
import com.miyu.module.dc.service.receive.ReceiveService;
import com.miyu.module.dc.service.receive.ReceiveServiceImpl;
import com.miyu.module.mqtt.Config.MqttConfiguration;
import com.miyu.module.mqtt.Config.MyMQTTClient;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dc.enums.ErrorCodeConstants.DC_CODE_ERROR;


public class MyMQTTCallback implements MqttCallbackExtended {


    private ReceiveService receiveService = SpringUtils.getBean(ReceiveService.class);

    //手动注入
    private MqttConfiguration mqttConfiguration = SpringUtils.getBean(MqttConfiguration.class);

    private static final Logger log = LoggerFactory.getLogger(MyMQTTCallback.class);

    private MyMQTTClient myMQTTClient;

    public MyMQTTCallback(MyMQTTClient myMQTTClient) {
        this.myMQTTClient = myMQTTClient;
    }


    /**
     * 丢失连接，可在这里做重连
     * 只会调用一次
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("mqtt connectionLost 连接断开，5S之后尝试重连: {}", throwable.getMessage());
        long reconnectTimes = 1;
        while (true) {
            try {
                if (MyMQTTClient.getClient().isConnected()) {
                    //判断已经重新连接成功  需要重新订阅主题 可以在这个if里面订阅主题  或者 connectComplete（方法里面）  看你们自己选择
                    log.warn("mqtt reconnect success end  重新连接  重新订阅成功");
                    return;
                }
                reconnectTimes+=1;
                log.warn("mqtt reconnect times = {} try again...  mqtt重新连接时间 {}", reconnectTimes, reconnectTimes);
                MyMQTTClient.getClient().reconnect();
            } catch (MqttException e) {
                log.error("mqtt断连异常", e);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
            }
        }
    }

    /**
     * @param topic
     * @param mqttMessage
     * @throws Exception
     * subscribe后得到的消息会执行到这里面
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("接收消息主题 : {}，接收消息内容 : {}", topic, new String(mqttMessage.getPayload()));

        JSONObject jsonObject = new JSONObject(mqttMessage.toString());
        String code = jsonObject.toString();
        if(code==null){
            throw exception(DC_CODE_ERROR);
        }else {
            receiveService.ReceiveHandle(topic, code);
        }


        //发布消息主题
//        if (topic.equals("embed/resp")){
//            Map maps = (Map) JSON.parse(new String(mqttMessage.getPayload(), CharsetUtil.UTF_8));
//            //业务接口------------------------------------------------------------------------------------
//            insertCmdResults(maps);
//        }
//        //接收报警主题
//        if (topic.equals("embed/warn")){
//            Map maps = (Map) JSON.parse(new String(mqttMessage.getPayload(), CharsetUtil.UTF_8));
//            //业务接口------------------------------------------------------------------------------------
//            insertPushAlarm(maps);
//        }
    }


    /**
     *连接成功后的回调 可以在这个方法执行 订阅主题  生成Bean的 MqttConfiguration方法中订阅主题 出现bug
     *重新连接后  主题也需要再次订阅  将重新订阅主题放在连接成功后的回调 比较合理
     * @param reconnect
     * @param serverURI
     */
    @Override
    public  void  connectComplete(boolean reconnect,String serverURI){
        log.info("MQTT 连接成功，连接方式：{}",reconnect?"重连":"直连");
        //订阅主题
        myMQTTClient.subscribe(mqttConfiguration.getTopic1(), 1);
//        myMQTTClient.subscribe(mqttConfiguration.getTopic2(), 1);
//        myMQTTClient.subscribe(mqttConfiguration.topic3, 1);
//        myMQTTClient.subscribe(mqttConfiguration.topic4, 1);
    }

    /**
     * 消息到达后
     * subscribe后，执行的回调函数
     *
     * @param s
     * @param mqttMessage
     * @throws Exception
     */
    /**
     * publish后，配送完成后回调的方法
     *
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("==========deliveryComplete={}==========", iMqttDeliveryToken.isComplete());
    }

}
