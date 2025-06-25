package com.miyu.module.eg.api.modbus;

import com.miyu.cloud.eg.api.modbus.ModbusApi;
import com.miyu.module.eg.modbus.ModbusService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

import static com.miyu.cloud.eg.enums.ModbusCodeConstans.*;

@RestController
@Validated
public class ModbusApiImpl implements ModbusApi {

    @Resource
    private ModbusService modbusService;

    /**
     * modebus测试  后续可以修改为其他功能
     * @param ipAddress
     * @param hexData
     * @return
     */
    @Override
    public String updateModbus(String ipAddress, String hexData) {
        try {
            return modbusService.sendWriteRequest( ipAddress, 1, 5, hexData);
        } catch (IOException e) {
            throw new RuntimeException("发送modbus请求失败");
        }
    }

    /**
     * modbus开门
     * @param ipAddress
     * @return
     */
    @Override
    public String openDoor(String ipAddress) {
        try {
            String code = modbusService.sendWriteRequest( ipAddress, 1, 5, MODBUS_CODE_CONTROLLER_OPEN );
            if (code.equals(MODBUS_CODE_CONTROLLER_OPEN)) {
                return "开门成功";
            }else {
                return "开门失败";
            }
        } catch (IOException e) {
            throw new RuntimeException("发送modbus请求失败");
        }
    }

    /**
     * modbus关门
     * @param ipAddress
     * @return
     */
    @Override
    public String closeDoor(String ipAddress) {
        try {
            String code = modbusService.sendWriteRequest( ipAddress, 1, 5, MODBUS_CODE_CONTROLLER_CLOSE);
            if (code.equals(MODBUS_CODE_CONTROLLER_CLOSE)) {
                return "关门成功";
            }else {
                return "关门失败";
            }
        } catch (IOException e) {
            throw new RuntimeException("发送modbus请求失败");
        }
    }

    /**
     * modbus模式切换
     * @param ipAddress
     * @return
     */
    @Override
    public String SwitchDoor(String ipAddress) {
        try {
            String code = modbusService.sendWriteRequest( ipAddress, 1, 5, MODBUS_CODE_CONTROLLER_MODE_SWITCH);
            if (code.equals(MODBUS_CODE_CONTROLLER_MODE_SWITCH)) {
                return "切换模式成功";
            }else {
                return "切换模式失败";
            }
        } catch (IOException e) {
            throw new RuntimeException("发送modbus请求失败");
        }
    }

    /**
     * modbus切换波特率  当前设置为4800 如需修改查看ModbusCodeConstans
     * @param ipAddress
     * @return
     */
    @Override
    public String updateBytes(String ipAddress) {
        try {
            String code = modbusService.sendWriteRequest( ipAddress, 1, 5, MODBUS_CODE_BTYE_4800);
            if (code.equals(MODBUS_CODE_BTYE_4800)) {
                return "modbus波特率修改为4800";
            }else {
                return "波特率修改失败";
            }
        } catch (IOException e) {
            throw new RuntimeException("发送modbus请求失败");
        }
    }

    /**
     * modbus查询地址
     * @param ipAddress
     * @return
     */
    @Override
    public String selectAddress(String ipAddress) {
        try {
            return modbusService.sendReadRequest( ipAddress, 1, 3, MODBUS_SELECT_IP);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modbus查询状态
     * @param ipAddress
     * @return
     */
    @Override
    public String selectState(String ipAddress) {
        try {
            String code =  modbusService.sendReadRequest( ipAddress, 1, 3, MODBUS_SELECT_STATUS);
            switch (code) {
                case MODBUS_STATUS_NORMAL:
                    return "正常状态";
                case MODBUS_STATUS_OPEN:
                    return "开门中";
                case MODBUS_STATUS_CLOSE:
                    return "关门中";
                case MODBUS_STATUS_OPENED:
                    return "开到位";
                case MODBUS_STATUS_CLOSED:
                    return "关到位";
                case MODBUS_STATUS_FAULT:
                    return "故障";
                default:
                    return "响应异常,请联系管理员";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
