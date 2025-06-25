package com.miyu.module.eg.modbus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import org.springframework.stereotype.Service;

import static cn.hutool.core.util.CharUtil.isHexChar;

@Service
public class ModbusService {

    @Autowired
    private DeviceManager deviceManager;

    /**
     * 向指定IP地址设备发送写请求。
     *
     * @param ipAddress 设备的IP地址
     * @param slaveId 从站ID
     * @param functionCode 功能码 （单线圈为5）
     * @param hexData 请求数据（十六进制字符串）
     * @return 接收到的响应帧（十六进制字符串）
     * @throws IOException 如果发生网络错误
     */
    public String sendWriteRequest(String ipAddress, int slaveId, int functionCode, String hexData) throws IOException {
        int port = deviceManager.getPortByIp(ipAddress);
        if (port == -1) {
            //此处注意！！！ 
            throw new IllegalArgumentException("设备IP地址无效: " + ipAddress);
        }

        // 将十六进制字符串转换为字节数组
        byte[] requestFrame = hexStringToByteArray(hexData);
        byte[] responseFrame = new ModbusRTURawCommunication().sendReceiveRawFrame(ipAddress, port, requestFrame);

        // 将字节数组转换为十六进制字符串并返回
        return byteArrayToHexString(responseFrame);
    }

    /**
     * 向指定IP地址设备发送读请求。
     *
     * @param ipAddress 设备的IP地址
     * @param slaveId 从站ID
     * @param functionCode 功能码
     * @param hexData 请求数据（十六进制字符串）
     * @return 接收到的响应帧（十六进制字符串）
     * @throws IOException 如果发生网络错误
     */
    public String sendReadRequest(String ipAddress, int slaveId, int functionCode, String hexData) throws IOException {
        int port = deviceManager.getPortByIp(ipAddress);
        if (port == -1) {
            throw new IllegalArgumentException("设备IP地址无效: " + ipAddress);
        }

        // 将十六进制字符串转换为字节数组
        byte[] requestFrame = hexStringToByteArray(hexData);
        byte[] responseFrame = new ModbusRTURawCommunication().sendReceiveRawFrame(ipAddress, port, requestFrame);

        // 将字节数组转换为十六进制字符串并返回
        String responseHex = byteArrayToHexString(responseFrame);
        System.out.println("接收到的响应帧: " + responseHex);
        return responseHex;
    }

    /**
     * 将十六进制字符串转换为字节数组。
     *
     * @param s 十六进制字符串
     * @return 字节数组
     */
    private byte[] hexStringToByteArray(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("16进制字符串不能为空");
        }

        // 去除可能存在的空格
        s = s.replaceAll("\\s+", "");

        int len = s.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("16进制字符串长度错误");
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i + 1);
            if (!isHexChar(c1) || !isHexChar(c2)) {
                throw new IllegalArgumentException("16进制字符串无效: " + s);
            }
            data[i / 2] = (byte) ((Character.digit(c1, 16) << 4) + Character.digit(c2, 16));
        }
        return data;
    }

    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}