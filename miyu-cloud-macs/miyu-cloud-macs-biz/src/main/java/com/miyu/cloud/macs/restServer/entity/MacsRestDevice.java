package com.miyu.cloud.macs.restServer.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MacsRestDevice {

    //设备编码
    private String code;
    //设备ip
    private String ip;
    //设备端口号
    private String port;
    //账号
    private String accountNumber;
    //密码
    private String password;

    //设备状态
    private Integer status = 0;

    private List<MacsRestDoor> doors;

    public MacsRestDevice() {
    }

    public MacsRestDevice(DeviceDO device) {
        this.code = device.getCode();
        this.ip = device.getIp();
        this.port = device.getPort();
        this.accountNumber = device.getAccountNumber();
        this.password = device.getPassword();
    }

    public void addDoor(MacsRestDoor restDoor) {
        if (this.doors == null) this.doors = new ArrayList<>();
        this.doors.add(restDoor);
    }

    public void createInstruct(DeviceDO device, DoorDO door, CollectorDO collector, MacsRestDoor.instructType instructType) {
        MacsRestDoor restDoor = new MacsRestDoor();
        MacsRestCollector restCollector = new MacsRestCollector();
        this.setCode(device.getCode());
        this.addDoor(restDoor);
        restDoor.setCode(door.getCode());
        restDoor.collectorsAdd(restCollector);
        restDoor.setInstruct(instructType);
        restCollector.setCode(collector.getCode());
        restCollector.setIndexPort(collector.getDevicePort());
    }
}
