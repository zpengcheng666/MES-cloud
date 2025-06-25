package com.miyu.cloud.macs.controller.admin.region.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 区域 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RegionRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3301")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "区域编码")
    @ExcelProperty("区域编码")
    private String code;

    @Schema(description = "区域名称", example = "李四")
    @ExcelProperty("区域名称")
    private String name;

    @Schema(description = "公开状态(0未公开,1公开)", example = "2")
    @ExcelProperty("公开状态(0未公开,1公开)")
    @DictFormat("publicStatus")
    private Boolean publicStatus;

    @Schema(description = "描述", example = "你说的对")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "上级id", example = "20787")
    @ExcelProperty("上级id")
    private String parentId;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @ExcelProperty("更新人")
    private String updateBy;

}