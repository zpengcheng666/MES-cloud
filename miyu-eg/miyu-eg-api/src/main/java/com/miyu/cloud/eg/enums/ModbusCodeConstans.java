package com.miyu.cloud.eg.enums;

public interface ModbusCodeConstans {

    //----------------根据实际需求修改使用(控制指令)-----------------------------------------------------------------------
    //------配置地址 有效地址范围为 1 - 254，每台控制器必须有唯一的通信地址，出厂默认为 0x01 请根据实际需求自行设置。
    //1 号控制器配置地址为0x96(150 号) 01 05 10 01 00 96 18 A4
    String MODBUS_CODE_CONTROLLER_ADDRESS_01 = "01 05 10 01 00 96 18 A4";
    //150 号控制器配置地址为0x01(1 号) 96 05 10 01 00 01 45 ED
    String MODBUS_CODE_CONTROLLER_ADDRESS_150 = "96 05 10 01 00 01 45 ED";

    //--------------------以下可直接使用(控制指令)------------------------------------------------------------------------
    //------控制器波特率 ！！！Ebyte推荐使用为38400(Ebyte为串口服务器 不是门闸!!!!! 根据门闸实际需求修改) 配完成后不建议通过代码修改 可以直接通过地址或配置软件修改！！！
    //1 号控制器配置波特率为4800bps
    String MODBUS_CODE_BTYE_4800 = "01 05 10 02 00 01 A9 0A";
    //1 号控制器配置波特率为9600bps
    String MODBUS_CODE_BTYE_9600 = "01 05 10 02 00 02 E9 0B";
    //1 号控制器配置波特率为19200bps
    String MODBUS_CODE_BTYE_19200 = "01 05 10 02 00 03 28 CB";
    //1 号控制器配置波特率为38400bps
    String MODBUS_CODE_BTYE_38400 = "01 05 10 02 00 04 61 9B";
    //------配置器手动模式是否可控 建议按照出厂使用 不建议修改
    //1 号控制器配置为手动模式可控
    String MODBUS_CODE_MANUAL_MODE_CONTROLLABLE = "01 05 10 03 00 00 39 0A";
    //1 号控制器配置为手动模式不可控
    String MODBUS_CODE_MANUAL_MODE_UNCONTROLLABLE = "01 05 10 03 00 01 F8 CA";
    //------控制器开关门 当使用控制指令时若应答码为0XE0,说明当前控制器不可控制。
    //------解决方式:1、将控制器改为自动模式。2、发送控制器配置指令将控制器配置为手动可控。
    //1 号控制器开门
    String MODBUS_CODE_CONTROLLER_OPEN = "01 05 00 04 FF 00 CD FB";
    //1 号控制器关门
    String MODBUS_CODE_CONTROLLER_CLOSE = "01 05 00 05 FF 00 9C 3B";
    //1 号控制器停止
    String MODBUS_CODE_CONTROLLER_STOP = "01 05 00 0A FF 00 AC 38";
    //1 号控制器模式切换(手/自动)
    String MODBUS_CODE_CONTROLLER_MODE_SWITCH = "01 05 00 0B 00 00 BC 08";

    //--------------------以下根据实际情况使用(查询指令)------------------------------------------------------------------------
    //查询控制器地址
    String MODBUS_SELECT_IP = "01 03 20 00 00 04 4F C9";
    //查询控制器状态
    String MODBUS_SELECT_STATUS = "01 03 20 02 00 01 B8 D7";
    //控制器返回状态(正常状态)
    String MODBUS_STATUS_NORMAL = "01 03 02 00 00 B8 D7";
    //控制器返回状态(开门中)
    String MODBUS_STATUS_OPEN = "01 03 02 00 01 E9 90";
    //控制器返回状态(关门中)
    String MODBUS_STATUS_CLOSE = "01 03 02 00 02 D9 53";
    //控制器返回状态(开到位)
    String MODBUS_STATUS_OPENED = "01 03 02 00 09 69 16";
    //控制器返回状态(关到位)
    String MODBUS_STATUS_CLOSED = "01 03 02 00 0A 59 5F";
    //控制器返回状态(故障)
    String MODBUS_STATUS_FAULT = "01 03 02 00 0B 49 A2";
}
