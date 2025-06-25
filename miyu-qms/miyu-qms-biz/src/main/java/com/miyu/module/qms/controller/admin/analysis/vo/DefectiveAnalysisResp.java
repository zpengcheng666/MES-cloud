package com.miyu.module.qms.controller.admin.analysis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 缺陷分析 VO")
@Data
@ToString(callSuper = true)
public class DefectiveAnalysisResp {


    private String name;

    private Integer value;
}
