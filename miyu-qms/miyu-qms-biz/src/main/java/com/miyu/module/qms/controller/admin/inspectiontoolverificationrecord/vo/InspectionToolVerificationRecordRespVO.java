package com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验工具校准记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionToolVerificationRecordRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5001")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "库存主键", example = "4249")
    @ExcelProperty("库存主键")
    private String stockId;

    @Schema(description = "工具主键", example = "13328")
    private String toolId;

    @Schema(description = "工具名称", example = "卡尺")
    @ExcelProperty("工具名称")
    private String toolName;

    @Schema(description = "送检日期")
    @ExcelProperty("送检日期")
    private LocalDateTime verificationDateBegin;

    @Schema(description = "实际送检时间")
    @ExcelProperty("实际送检时间")
    private LocalDateTime verificationDateBeginAct;

    @Schema(description = "完成时间")
    @ExcelProperty("完成时间")
    private LocalDateTime verificationDateEnd;

    @Schema(description = "送检结果", example = "合格")
    @ExcelProperty("送检结果")
    private String remark;

    @Schema(description = "状态", example = "1")
    @ExcelProperty("状态")
    private Integer status;

}
