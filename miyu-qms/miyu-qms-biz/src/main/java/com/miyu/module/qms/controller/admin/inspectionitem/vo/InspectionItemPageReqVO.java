package com.miyu.module.qms.controller.admin.inspectionitem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检测项目分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionItemPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

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

    @Schema(description = "物料类型ID")
    private String materialConfigId;

}