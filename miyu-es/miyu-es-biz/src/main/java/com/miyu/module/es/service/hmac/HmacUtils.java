package com.miyu.module.es.service.hmac;

import com.alibaba.fastjson.JSONObject;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNDataVO;
import com.miyu.module.es.controller.admin.brakeN.vo.QueryCondition;
import com.miyu.module.es.dal.dataobject.monthlyCar.MonthlyCarData;
import com.miyu.module.es.dal.dataobject.monthlyCar.MonthlyData;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.hash.Hashing.hmacSha256;

@Configuration
public class HmacUtils {

    /**
     * Sign加密
     */
    public String getSign(Object object , String nonce , long s){

        String ascii = this.convertDataToString(object);

        //ASCII排序后 转为小写后MD5加密
        String md =  DigestUtils.md5Hex(ascii.toLowerCase());

        return this.getSign(md,nonce,s);
    }

    /**
     * 生成body
     */
    public String getBody(long s , Object object){
        //生成Nonce
        String nonce = RandomStringUtils.random(10, false, true);

        //生成Sign
        String sign = this.getSign(object , nonce ,s);

        //数据处理
        String jsonStr = JSONObject.toJSONString(object);
        JSONObject jsonObjectData = JSONObject.parseObject(jsonStr);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ParkKey", "1731331709324685");
        jsonObject.put("AppId", "ncegrzby");
        jsonObject.put("TimeStamp", s);
        jsonObject.put("Nonce",nonce);
        jsonObject.put("Sign", sign);
        jsonObject.put("Data", jsonObjectData);

        return jsonObject.toString();
    }

    /**
     * 将Data进行ASCII码从小到大排序（升序）后转换为字符串
     *
     * @param data 请求数据
     * @return 转换后的字符串
     */
    private static String convertDataToString(Object data) {
        // 如果数据为空，返回空字符串
        if (data == null) {
            return "";
        }

        // 如果数据是数组或者集合
        if (data instanceof Collection<?> || data.getClass().isArray()) {
            // 将数组或集合转换为列表
            List<Object> dataList = new ArrayList<>();
            if (data.getClass().isArray()) {
                dataList = Arrays.asList((Object[]) data);
            } else {
                dataList = new ArrayList<>((Collection<?>) data);
            }
            // 使用递归的方式对列表中的每个元素进行处理
            List<String> dataDictList = dataList.stream()
                    .map(HmacUtils::convertDataToString)
                    .collect(Collectors.toList());
            // 对集合使用稳定排序，保证相同的元素不会交换位置
            dataDictList.sort(Comparator.naturalOrder());
            // 将每个元素转换为一个字符串，并使用&符号连接起来
            return String.join("&", dataDictList);
        }

        // 如果数据是对象
        // 使用反射获取对象的属性值，并将其转换为一个键值对序列
        Map<String, Object> dataDict = new TreeMap<>();
        Class<?> clazz = data.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    // 获取属性值
                    Object value = field.get(data);
                    // 如果属性值是数组或集合，使用递归的方式进行处理
                    if (value != null && (value.getClass().isArray() || value instanceof Collection<?>)) {
                        value = convertDataToString(value);
                    }
                    // 添加键值对到字典中
                    if (value != null) {
                        dataDict.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field: " + field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }

        // 将字典转换为一个字符串，并使用&符号连接起来
        return String.join("&", dataDict.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList()));
    }

    /**
     * ASCII排序后 转为小写
     * @param data
     * @return
     */
    public static String sortAndFormatParams(Map<String, String> data) {
        //执行
        Map<String, Object> dataMap = new TreeMap<>(data);
        String dataS = dataMap.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
        return dataS.toLowerCase();
    }

    /**
     * 生成Sign签名字符串
     */
    public String getSign(String md , String nonce , long s) {
        String mat = String.format("AppId=%s&Data=%s&ParkKey=%s&TimeStamp=%s&Nonce=%s", "ncegrzby", md, "1731331709324685", s, nonce).toLowerCase();
        return this.toHmacSha256(mat);
    }


    /**
     * hmacSha256格式认证
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String toHmacSha256(String data) {

        String secret = "20xeabt4g9w7kcgi";

        try {
            String result = hmacSha256(secret, data);
            System.out.println("Result: " + result);
            return result.toLowerCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * hmacSha256加密
     * @param secret
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String hmacSha256(String secret, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] messageBytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        Mac hmacsha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        hmacsha256.init(secretKey);

        byte[] hashMessage = hmacsha256.doFinal(messageBytes);
        String result = Base64.getEncoder().encodeToString(hashMessage);
        return result.toLowerCase();
    }

    /**
     * 车牌认证
     * @param licensePlate
     * @return
     */
    public String determineLicensePlateColor(String licensePlate) {
        if (licensePlate == null || licensePlate.isEmpty()) {
            return "1729785333447363";
        }
        // 这里只是简单地通过车牌号码的格式来猜测颜色
        // 实际应用中应该有更复杂的逻辑来处理各种情况
        if (licensePlate.matches("[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵青藏川宁琼港澳]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{5}$")) {
            // 假设蓝色车牌是默认情况
            return "1731331709330375";
        } else if (licensePlate.matches("黄") || licensePlate.length() > 7) { // 大型车辆或特殊标识
            return "1731331709330179";
        } else if (licensePlate.matches("[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵青藏川宁琼]{1}[A-Z]{1}警[0-9]{4}$") || licensePlate.matches("WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵青藏川宁琼]{1}[0-9]{5}$")) { // 军用车牌
            return "1731331709330334";
        } else if (licensePlate.matches("使[0-9]{3}[A-Z]{1}$")) { // 领事馆等
            return "1731331709330625";
        } else if (licensePlate.matches("[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵青藏川宁琼]{1}[D|F]{1}[A-HJ-NP-Z0-9]{5}$")){ // 新能源车牌
            return "1731331709330612";
        } else {
            return "1731331709330131";
        }
    }

    /**
     * ASE解密
     */
    public String aesDecrypt(String value) throws Exception {

        String key = "infoexchange4api";

        String iv = "activevector4api";

        // 将密钥转换为字节数组
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        // 将经 Base64 编码的加密值转换为字节数组
        byte[] encryptedBytes = Base64.getDecoder().decode(value);

        // 创建 SecretKeySpec 对象
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // 设置 IV，使用提供的值或密钥的前 16 个字符
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

        // 创建 Cipher 对象并初始化
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 执行解密操作
        byte[] resultArray = cipher.doFinal(encryptedBytes);

        // 将结果字节数组转换为 UTF-8 编码的字符串
        return new String(resultArray, StandardCharsets.UTF_8);
    }


}
