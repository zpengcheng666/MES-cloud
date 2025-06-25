package com.miyu.module.qms.controller.admin.managementdatabase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 质量管理资料库 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ManagementDatabaseRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22956")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "所属质量管理关联ID", example = "25724")
    @ExcelProperty("所属质量管理关联ID")
    private String treeId;

    @Schema(description = "类型", example = "2")
    @ExcelProperty("类型")
    private Integer type;

    @Schema(description = "附件", example = "https://www.iocoder.cn")
    private List<String> fileUrl;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "状态", example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "工作流编号", example = "14023")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}