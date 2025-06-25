package com.miyu.cloud.es.api.brakeN.dto;

import lombok.Data;

@Data
public class StopSpace {

    /**
     * 记录编号
     */
    private String No;

    /**
     * 停车区域类型
     */
    private Integer Type;


    /**
     * 可停区域编号数组
     */
    private String AreaNos;


    /**
     * 简略显示可停区域名称
     */
    private String AreaName;

    /**
     * 区域可停车位数
     */
    private Integer SpaceCount;
}
