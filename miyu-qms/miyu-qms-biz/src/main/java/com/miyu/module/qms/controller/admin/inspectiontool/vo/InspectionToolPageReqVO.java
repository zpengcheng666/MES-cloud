package com.miyu.module.qms.controller.admin.inspectiontool.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检测工具分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionToolPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "检测工具名称", example = "张三")
    private String name;

    @Schema(description = "制造商", example = "张三")
    private String manufacturer;

    @Schema(description = "出厂编号", example = "张三")
    private String manufacturerNumber;

    @Schema(description = "本厂编号", example = "张三")
    private String barCode;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "检/校准日期", example = "张三")
    private LocalDateTime verificationDate;

}