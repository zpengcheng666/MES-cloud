package com.miyu.module.wms.controller.admin.carrytask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 搬运任务下发任务 VO")
@Data
public class CarryTaskIssueVO {
    /**
     * 任务编号
     */
    private String TaskNo;
    /**
     * 托盘类型
     */
    private String ContainerType;
    /**
     * 优先级
     */
    private Integer Priority;
    /**
     * 子任务集合
     */
    private List<CarrySubTaskIssueVO> SiteList;
}
