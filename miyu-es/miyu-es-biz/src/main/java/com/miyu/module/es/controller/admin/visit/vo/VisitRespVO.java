package com.miyu.module.es.controller.admin.visit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 访客记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VisitRespVO {

    @Schema(description = "访问记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22309")
    @ExcelProperty("访问记录ID")
    private String visitRecordId;

    @Schema(description = "访客姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("访客姓名")
    private String name;

    @Schema(description = "访客车牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "访客车牌不能为空")
    private String licenseNo;

    @Schema(description = "访客签退时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("访客签退时间")
    private LocalDateTime visitorCancelTime;

    @Schema(description = "访问记录状态(1：创建访客访问记录 2：取消访客访问记录 3：签到)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "访问记录状态(1：创建访客访问记录 2：取消访客访问记录 3：签到)", converter = DictConvert.class)
    @DictFormat("es_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "访客单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("访客单位")
    private String company;

    @Schema(description = "来访目的", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("来访目的")
    private String cause;

    @Schema(description = "同行人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1411")
    @ExcelProperty("同行人数")
    private String followCount;

    @Schema(description = "计划开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划开始时间")
    private LocalDateTime planBeginTime;

    @Schema(description = "计划结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划结束时间")
    private LocalDateTime planEndTime;

    @Schema(description = "访客签到时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("访客签到时间")
    private LocalDateTime visitorRecordTime;

    @Schema(description = "访客签到码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("访客签到码")
    private String visitorCheckCode;

    @Schema(description = "被访人tpId", requiredMode = Schema.RequiredMode.REQUIRED, example = "320")
    @ExcelProperty("被访人tpId")
    private String visitTpId;

    @Schema(description = "设备 SN", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("设备 SN")
    private String deviceSn;

}