package com.miyu.module.wms.framework.generator.seivice;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ICodeGeneratorService {


    String getMaterialBarCode();

    String getMaterialBatchNumber();

    String getIOM_Number();

    /**
     * 根据规则生成指定数量的编码
     *
     * @param ruleCode    规则
     * @param ruleName    规则
     * @param codes   编码
     * @param names   名称
     * @param lengths 长度
     * @return 生成的编码
     */
    Map<String, Object> generateCodes(String ruleCode, String ruleName, String[] codes, String[] names, Integer[] lengths);

    /**
     * 生成流水号
     *
     * @param prefix 前缀 如：WMS-
     * @return 流水号
     */
    String generateTimestampSerialNumber(String prefix);

    String generateTimestampSerialNumber();

    /**
     * 生成批次号
     *
     * @param prefix 前缀 如：BAT-
     * @return 批次号
     */
    String generateBatchNumber(String prefix);
}
