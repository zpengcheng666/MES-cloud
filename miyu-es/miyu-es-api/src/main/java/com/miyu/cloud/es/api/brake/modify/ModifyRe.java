package com.miyu.cloud.es.api.brake.modify;

import lombok.Data;

@Data
public class ModifyRe {

    /**
     * 接口命令标识
     */
    private String command;

    /**
     * 停车场在客户管理系统对应的唯一ID，可在H2后台(业务管理->接口设置)进行设置
     */
    private String parkingId;

    /**
     * 消息ID，停车场系统生成本消息编号，回应报文返回该消息ID
     */
    private String messageId;

    /**
     * 时间戳（格式：yyyyMMddHHmmss）
     */
    private String timestamp;

    /**
     * 业务参数
     */
    private ModifyReData content;
}
