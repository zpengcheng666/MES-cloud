package com.miyu.cloud.macs.restServer.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MacsRestDoor {

    //门编码
    private String code;
    //门连接口
    private Integer indexPort;
    //执行指令
    private instructType instruct;

    //门禁状态
    private Integer doorStatus = 2;

    //采集器
    private List<MacsRestCollector> collectors;

    public MacsRestDoor() {
    }

    public MacsRestDoor(DoorDO door) {
        this.code = door.getCode();
        this.indexPort = door.getDevicePort();
    }

    public enum instructType {
        open,//长开
        close,//关
        general//通行一次
    }

    public void collectorsAdd(MacsRestCollector collector) {
        if (this.collectors == null) this.collectors = new ArrayList<>();
        this.collectors.add(collector);
    }
}
