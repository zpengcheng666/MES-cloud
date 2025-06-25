package com.miyu.module.wms.controller.admin.checkplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 库存盘点计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CheckPlanRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "盘点库区id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("盘点库区id")
    private String checkAreaId;

    @Schema(description = "盘点名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("盘点名称")
    private String checkName;

    @Schema(description = "物料类型ids")
    @ExcelProperty("物料类型ids")
    private List<String> materialConfigIds;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("截止时间")
    private LocalDateTime cutOffTime;

    @Schema(description = "负责人")
    @ExcelProperty("负责人")
    private String checkUserId;

    @Schema(description = "盘点状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "盘点状态", converter = DictConvert.class)
    @DictFormat("check_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer checkStatus;

    @Schema(description = "是否锁盘", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "是否锁盘", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean checkLocked;


    private List<String> materialConfigNumbers;

    private String areaCode;

}