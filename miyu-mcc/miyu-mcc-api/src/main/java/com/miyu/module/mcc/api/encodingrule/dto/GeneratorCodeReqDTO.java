package com.miyu.module.mcc.api.encodingrule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Schema(description = "RPC 服务 - 销售系统  发货单明细")
@Data
public class GeneratorCodeReqDTO {

    /**
     * 编码分类  （正常请求 以及生成半成品类型的时候调用  参考MccConstants类）
     */
    private String classificationCode;


    /***
     * 主物料类别（物料类型生成时用）
     */
    private String materialMainTypeCode;


    /**
     * 类型  1普通码 2追加码
     */
    private Integer encodingRuleType =1;

    /***
     * 自定义参数
     * key   sourceCode   来源号（如  物料类型码（mcc_material表material_code）   ）
     *    partNumber   图号
     *     procedureNum 工序
     *     materialTypeCode  物料类码（mcc_material_type表code)
     */
    private Map<String,String> attributes;


    /****
     * 根据旧码生新码 时入参
     * 挂关系时入参
     */
    private String oldBarCode;
}
