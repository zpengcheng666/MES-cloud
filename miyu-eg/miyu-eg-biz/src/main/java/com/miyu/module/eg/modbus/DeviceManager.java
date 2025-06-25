package com.miyu.module.eg.modbus;

import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Component
public class DeviceManager {

    @Autowired
    private McsManufacturingControlApi mcsManufacturingControlApi;

    private static final int PORT = 8887; // 所有设备的端口号相同
    private Map<String, Integer> devicePorts = new HashMap<>();
    private Map<String, Socket> deviceConnections = new HashMap<>();

    /**
     * 构造函数。
     */
    public DeviceManager() {
        // 构造函数保持为空，所有的初始化工作将在 init 方法中完成
    }

    /**
     * 初始化设备IP地址和端口号。
     */
    @PostConstruct
    public void init() {
        if (mcsManufacturingControlApi == null) {
            throw new IllegalStateException("MCS服务初始化失败");
        }

        //正式使用
//        String[] deviceIPs = mcsManufacturingControlApi.getEgLedger().getData();
        //测试使用
        String[] deviceIPs = new String[]{"192.168.3.7"};

        if (deviceIPs == null || deviceIPs.length == 0) {
            throw new IllegalStateException("找不到设备Ip");
        }

        for (String ipAddress : deviceIPs) {
            devicePorts.put(ipAddress, PORT);
        }
    }

    /**
     * 获取设备的端口号。
     *
     * @param ipAddress 设备的IP地址
     * @return 端口号
     */
    public int getPortByIp(String ipAddress) {
        return devicePorts.getOrDefault(ipAddress, -1);
    }

    /**
     * 项目启动时初始化设备连接。
     */
    @PostConstruct
    public void initDevices() {
        for (Map.Entry<String, Integer> entry : devicePorts.entrySet()) {
            String ipAddress = entry.getKey();
            int port = entry.getValue();
            try {
                connectToDevice(ipAddress, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 项目关闭时断开所有设备连接。
     */
    @PreDestroy
    public void closeDevices() {
        for (String ipAddress : deviceConnections.keySet()) {
            disconnectFromDevice(ipAddress);
        }
    }

    /**
     * 连接到指定IP地址的设备。
     *
     * @param ipAddress 设备的IP地址
     * @param port 设备的端口号
     * @throws IOException 如果连接失败则抛出异常
     */
    public void connectToDevice(String ipAddress, int port) throws IOException {
        Socket socket = new Socket(ipAddress, port);
        socket.setSoTimeout(ModbusRTURawCommunication.TIMEOUT);
        deviceConnections.put(ipAddress, socket);
        System.out.println("成功连接到设备: " + ipAddress);
    }

    /**
     * 断开与指定IP地址设备的连接。
     *
     * @param ipAddress 设备的IP地址
     */
    public void disconnectFromDevice(String ipAddress) {
        if (deviceConnections.containsKey(ipAddress)) {
            try {
                deviceConnections.get(ipAddress).close();
                deviceConnections.remove(ipAddress);
                System.out.println("成功断开设备: " + ipAddress);
            } catch (IOException e) {
                System.err.println("关闭连接时发生错误: " + e.getMessage());
            }
        } else {
            System.err.println("设备未连接或连接已关闭: " + ipAddress);
        }
    }

    /**
     * 获取所有已连接设备的IP地址列表。
     *
     * @return 设备IP地址列表
     */
    public String[] getConnectedDeviceIPs() {
        return deviceConnections.keySet().toArray(new String[0]);
    }
}