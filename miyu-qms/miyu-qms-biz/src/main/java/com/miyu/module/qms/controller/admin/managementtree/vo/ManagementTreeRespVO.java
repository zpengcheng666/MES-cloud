package com.miyu.module.qms.controller.admin.managementtree.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 质量管理关联树 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ManagementTreeRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1389")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "父节点")
    @ExcelProperty("父节点")
    private String parent;

    @Schema(description = "节点关联字段ID", example = "1234")
    private String nodeId;

    @Schema(description = "节点名", example = "李四")
    @ExcelProperty("节点名")
    private String nodeName;

    @Schema(description = "类型", example = "1")
    private Integer nodeType;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}