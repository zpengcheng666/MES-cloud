package com.miyu.module.eg.modbus;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ModbusRTURawCommunication {

    public static final int TIMEOUT = 5000; // 设置超时时间（毫秒）

    /**
     * 发送 Modbus RTU 数据帧到指定 IP 地址的串口服务器，并接收响应。
     *
     * @param ipAddress 串口服务器的IP地址
     * @param port 串口服务器的端口号
     * @param requestFrame 要发送的 Modbus RTU 请求帧（字节数组）
     * @return 接收到的响应帧（字节数组）
     * @throws IOException 如果发生网络错误
     */
    public byte[] sendReceiveRawFrame(String ipAddress, int port, byte[] requestFrame) throws IOException {
        try (Socket socket = new Socket(ipAddress, port)) {
            socket.setSoTimeout(TIMEOUT); // 设置套接字超时时间

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            // 发送请求帧
            outputStream.write(requestFrame);
            outputStream.flush();

            // 接收响应帧
            byte[] buffer = new byte[256]; // 根据实际情况调整缓冲区大小
            int bytesRead = inputStream.read(buffer);

            if (bytesRead == -1) {
                throw new IOException("未从设备接收到任何数据");
            }

            return Arrays.copyOf(buffer, bytesRead);
        }
    }

    /**
     * 构建一个简单的 Modbus RTU 请求帧。
     *
     * @param slaveId 从站ID
     * @param functionCode 功能码
     * @param data 请求数据
     * @return 构建好的请求帧（字节数组）
     */
    public static byte[] buildRequestFrame(int slaveId, int functionCode, byte[] data) {
        byte[] frame = new byte[3 + data.length + 2]; // 3 字节头 + 数据长度 + 2 字节 CRC

        // 填充帧头
        frame[0] = (byte) slaveId;
        frame[1] = (byte) functionCode;

        // 填充数据
        System.arraycopy(data, 0, frame, 2, data.length);

        // 计算并填充 CRC 校验码
        int crc = calculateCRC(frame, 0, 2 + data.length);
        frame[frame.length - 2] = (byte) (crc & 0xFF);
        frame[frame.length - 1] = (byte) ((crc >> 8) & 0xFF);

        return frame;
    }

    /**
     * 计算 Modbus RTU CRC 校验码。
     *
     * @param data 数据数组
     * @param offset 数据偏移量
     * @param length 数据长度
     * @return 计算出的 CRC 校验码
     */
    private static int calculateCRC(byte[] data, int offset, int length) {
        int crc = 0xFFFF;
        for (int pos = offset; pos < offset + length; pos++) {
            crc ^= (data[pos] & 0xFF);
            for (int i = 8; i != 0; i--) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }
}