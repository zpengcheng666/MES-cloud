package com.miyu.module.qms.controller.admin.inspectionitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 检测项目新增/修改 Request VO")
@Data
public class InspectionItemSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17602")
    private String id;

    @Schema(description = "检测项目名称", example = "赵六")
    private String itemName;

    @Schema(description = "检测项目编号")
    private String itemNo;

    @Schema(description = "检测项目描述")
    private String content;

    @Schema(description = "检测方式  1定性 2定量", example = "1")
    private Integer inspectionType;

    @Schema(description = "检测工具  目测 皮尺测量 ")
    private String inspectionToolId;

    @Schema(description = "检测项目分类ID", example = "30665")
    private String itemTypeId;

}