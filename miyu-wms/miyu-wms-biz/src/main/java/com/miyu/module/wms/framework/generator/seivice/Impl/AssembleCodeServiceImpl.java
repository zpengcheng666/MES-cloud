package com.miyu.module.wms.framework.generator.seivice.Impl;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.api.encodingrule.dto.UpdateCodeReqDTO;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 组装编码实现类
 * @author Caixiaowei
 * @date 2021/12/13
 */
@Service
public class AssembleCodeServiceImpl implements ICodeGeneratorService {

    @Resource

    private EncodingRuleApi encodingRuleApi;
    // 生成物料流水号
    private final static String MATERIAL_BARCODE = "BC";
    // 生成物料批次号
    private final static String MATERIAL_BATCH = "BAT";
    // 出入库单号
    private final static String IOM = "IOM";

    /**
     * 生成物料条码
     * @return
     */
    @Override
    public String getMaterialBarCode() {
        GeneratorCodeReqDTO reqDTO = new GeneratorCodeReqDTO();
        reqDTO.setEncodingRuleType(1);
        reqDTO.setClassificationCode(MATERIAL_BARCODE);
        CommonResult<String> result = null;
        try {
            result = encodingRuleApi.generatorCode(reqDTO);
        } catch (InterruptedException e) {
            throw exception(MATERIAL_BARCODE_GENERATE_FAILED);
        }
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        try {
            encodingRuleApi.updateCodeStatus(new UpdateCodeReqDTO().setCode(result.getData()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result.getData();
    }

    /**
     * 生成批次号
     * @return
     */
    @Override
    public String getMaterialBatchNumber() {
        GeneratorCodeReqDTO reqDTO = new GeneratorCodeReqDTO();
        reqDTO.setEncodingRuleType(1);
        reqDTO.setClassificationCode(MATERIAL_BATCH);
        CommonResult<String> result = null;
        try {
            result = encodingRuleApi.generatorCode(reqDTO);
        } catch (InterruptedException e) {
            throw exception(MATERIAL_BATCH_CODE_GENERATE_FAILED);
        }
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        try {
            encodingRuleApi.updateCodeStatus(new UpdateCodeReqDTO().setCode(result.getData()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.getData();
    }

    @Override
    public String getIOM_Number() {
        GeneratorCodeReqDTO reqDTO = new GeneratorCodeReqDTO();
        reqDTO.setEncodingRuleType(1);
        reqDTO.setClassificationCode(IOM);
        CommonResult<String> result = null;
        try {
            result = encodingRuleApi.generatorCode(reqDTO);
        } catch (InterruptedException e) {
            throw exception(WAREHOUSE_ORDER_CODE_GENERATE_FAILED);
        }
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        try {
            encodingRuleApi.updateCodeStatus(new UpdateCodeReqDTO().setCode(result.getData()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.getData();
    }

    /**
     * 根据规则和参数生成编码列表
     * @param ruleCode 规则  “|” 为规则分割符号   如：#|#|C|-G|-L|-S
     * @param ruleName 规则  “|” 为规则分割符号   如：#|#|通道|-组|-层|-位
     * @param codes 编码固定前缀数组 如：CK01,KQ01
     * @param names 编码名称固定前缀数组 如：仓库01,库区01
     * @param lengths 编码长度数组 代表 通道 组 列 位 如：[4,4,4,4] 此数组参数可以为null 如：[4,4,null,4]
     * @return 代码列表 结果 CK01#KQ01#C1-G1-L1-S1                或              CK01#KQ01#C1-G1-S1
     */
    @Override
    public Map<String, Object> generateCodes(String ruleCode,String ruleName, String[] codes, String[] names, Integer[] lengths) {
        if(StringUtils.isBlank(ruleCode) || !ruleCode.contains("|")
                || StringUtils.isBlank(ruleName) || !ruleName.contains("|")
                || names.length == 0 || codes.length == 0 ||
                lengths == null || lengths.length == 0){
            throw new IllegalArgumentException("入参为空或格式错误");
        }
        // 初始化结果集
        List<String> codeResult = new ArrayList<>();
        List<String> nameResult = new ArrayList<>();
        List<Integer[]> numberResult = new ArrayList<>();

        // 解析规则 将连接符号取出来
        String[] splitC = ruleCode.split("\\|");
        String[] splitN = ruleName.split("\\|");
        // 初始化 递增数组
        List<Integer> IncreaseList = new ArrayList<>();
        // 初始化 循环次数
        int forMaxLength = 1;
        for (int i = 0; i < lengths.length; i++) {
            if(lengths[i] == null){
                // 为空的列也要放入 递增数组中
                IncreaseList.add(null);
            }else{
                // 初始化递增集合
                IncreaseList.add(1);
                // 计算循环次数
                forMaxLength *= lengths[i];
            }
        }
        // boolean 标记 递增编码第一位
        boolean flag = true;
        // 循环生成
        for (int l = 0; l < forMaxLength; l++) {
            // 循环生成字符串
            StringBuilder code = new StringBuilder();
            StringBuilder name = new StringBuilder();
            Integer[] number = new Integer[lengths.length];
            // 循环拼接字符串
            for (int i = 0; i < splitC.length; i++) {
                // 循环拼接字符串 取出固定前缀 如CK01 前缀不能为空
                if(i < codes.length && StringUtils.isNotBlank(codes[i])){
                    code.append(codes[i]);
                    code.append(splitC[i]);
                    name.append(names[i]);
                    name.append(splitN[i]);
                    flag = true;
                } else if(i >= codes.length){// 循环拼接字符串 取出连接符号 如-G ，-L， -S
                    // 计算递增数组下标
                    int index = i - codes.length;

                    number[index] = IncreaseList.get(index);

                    // 如此位置为null 则不用填充
                    if(IncreaseList.get(index) == null){
                        if(index == lengths.length - 1){
                            IncreaseList = getIncreaseMap(IncreaseList,lengths);
                        }
                        continue;
                    }
                    if(flag){
                        // 将连接符号 填入字符串中
                        code.append(splitC[i].substring(1));
                        name.append(splitN[i].substring(1));
                        flag = false;
                    }else {
                        // 将连接符号 填入字符串中
                        code.append(splitC[i]);
                        name.append(splitN[i]);
                    }

                    // 将其值 填入字符串中
                    code.append(IncreaseList.get(index));
                    name.append(IncreaseList.get(index));

                    // 循环至递增数组最后一位 计算递增数组
                    if(index == lengths.length - 1){
                        IncreaseList = getIncreaseMap(IncreaseList,lengths);
                    }
                }
            }
            codeResult.add(code.toString());
            nameResult.add(name.toString());
            numberResult.add(number);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("codes", codeResult);
        result.put("names", nameResult);
        result.put("numbers", numberResult);
        return result;
    }

    /**
     * 递增数组计算
     * @param increaseList 递增数组
     * @param lengths 长度数组
     * @return 递增数组
     */
    private static List<Integer> getIncreaseMap(List<Integer> increaseList, Integer[] lengths){
        if(increaseList.size() != lengths.length){
            throw new IllegalArgumentException("入参错误，数组长度与自增数组长度不一致");
        }
        // 循环计算 递增数组 从后往前
        int maxIndex = increaseList.size() - 1;
        for (int i = maxIndex; i >= 0; i--) {
            Integer increaseValue = increaseList.get(i);
            // 如此位置为null 则不用计算
            if(increaseValue == null){
                continue;
            }else if(increaseValue < lengths[i]){// 如此位置未达到最大值 则递增
                increaseList.set(i, ++increaseValue);
                break;
            }else {// 如此位置达到最大值 则置为1 重新计算 并继续循环 将此下标前一位值也递增
                increaseList.set(i, 1);
            }
        }
        return increaseList;
    }

    /**
     * 生成时间戳序列号
     * @return 时间戳序列号
     */
    @Override
    public String generateTimestampSerialNumber(String prefix) {
        // 获取当前时间戳（yyyyMMddHHmmss格式）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        // 生成一个随机数部分（0-9999），并格式化为4位数字
        Random random = new Random();
        int randomNumber = random.nextInt(10000); // 0到9999
        String randomPart = String.format("%04d", randomNumber);

        // 拼接时间戳和随机数
        String sequence = timestamp + randomPart;
//        LocalDateTime now = LocalDateTime.now();
//        String timestamp = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));
        return prefix + sequence;
    }
    @Override
    public String generateTimestampSerialNumber() {
        // 时间戳序列号 毫秒级
        return String.valueOf(System.currentTimeMillis());
//        return String.valueOf(Instant.now().getEpochSecond());
    }

    /**
     * 生成批次号
     * @return 批次号
     */
    @Override
    public String generateBatchNumber(String prefix) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + timestamp; // 在生成的时间戳前添加标识符
    }

    public static void main(String[] args) {
        Map<String, Object> codeMap = new AssembleCodeServiceImpl().generateCodes("#|#|-C|-G|-L|-S","#|#|-通道|-组|-层|-位", new String[]{"CK01", "KQ01"}, new String[]{"仓库01", "库区01"} , new Integer[]{null ,2, null, null});
        System.out.println(codeMap);;
        List<Integer[]> numbers = (List<Integer[]>)codeMap.get("numbers");
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(Arrays.asList(numbers.get(i)));
        }
//        System.out.println(generateBatchNumber());
    }
}
