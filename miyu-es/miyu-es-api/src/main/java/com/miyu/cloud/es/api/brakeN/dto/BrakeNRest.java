package com.miyu.cloud.es.api.brakeN.dto;

import lombok.Data;

@Data
public class BrakeNRest {

    /**
     * 请求结果代码
     */
    private Integer ResultCode;

    /**
     * 请求提示信息
     */
    private String ResultMsg;

    /**
     * 请求响应结果标识
     */
    private String ResultId;

    /**
     * 具体数据
     */
    private BrakeNData Data;
}
