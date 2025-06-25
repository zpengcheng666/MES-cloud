package com.miyu.cloud.macs.restServer.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MacsRestCollector {

    //采集器编码
    private String code;
    //采集器连接口
    private Integer indexPort;
    //采集器连信息(设备型号,连接参数等信息)
    private String connectionInformation;

    //采集器状态
    private Integer status = 0;
    //用户信息
    private MacsRestUser user;

    public MacsRestCollector() {
    }

    public MacsRestCollector(CollectorDO collector) {
        this.code = collector.getCode();
        this.indexPort = collector.getDevicePort();
        this.connectionInformation = collector.getConnectionInformation();
    }
}
