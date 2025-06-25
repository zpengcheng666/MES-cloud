package com.miyu.cloud.macs.controller.admin.visitorRegion.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 访客区域权限 Response VO")
@Data
@ExcelIgnoreUnannotated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisitorRegionRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21735")
    @ExcelProperty("主键id")
    private String id;

    private String regionCode;

    private String regionName;

    private String applicationNumber;

    private Integer status;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invalidDate;

}
