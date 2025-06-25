package com.miyu.module.es.controller.admin.brakeN.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class BrakeNDataVO {

    /**
     * 当页页码
     */
    @JSONField(name = "PageIndex")
    private Integer PageIndex;

    /**
     *每页数据量
     */
    @JSONField(name = "PageSize")
    private Integer PageSize;

    /**
     * 排序字段
     */
    @JSONField(name = "OrderBy")
    private String OrderBy;

    /**
     * 排序方式
     */
    @JSONField(name = "OrderType")
    private Integer OrderType;

    /**
     * 查询条件集合
     */
    @JSONField(name = "QueryCondition")
    private List<QueryCondition> QueryCondition;

}
