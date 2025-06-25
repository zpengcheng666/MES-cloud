package com.miyu.module.Influxdb.dal;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

import java.util.Map;

@Data
public class CommonCollection extends BaseDO {

    /**
     * 表
     */
    private String measurement;

    /**
     * 时间
     */
    private Long time;

    /**
     * 标签
     */
    private Map<String, String> tags;

    /**
     * 字段
     */
    private Map<String, Object> fields;
}
