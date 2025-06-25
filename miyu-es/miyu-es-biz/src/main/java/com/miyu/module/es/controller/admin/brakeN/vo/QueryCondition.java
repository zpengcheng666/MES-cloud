package com.miyu.module.es.controller.admin.brakeN.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class QueryCondition {

    /**
     * 查询字段
     */
    @JSONField(name = "Field")
    private String Field;

    /**
     * 查询操作符
     */
    @JSONField(name = "Operator")
    private Integer Operator;

    /**
     * 查询值
     */
    @JSONField(name = "Value")
    private String Value;
}
