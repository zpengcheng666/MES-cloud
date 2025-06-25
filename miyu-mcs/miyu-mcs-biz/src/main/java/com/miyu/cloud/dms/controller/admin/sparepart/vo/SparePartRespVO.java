package com.miyu.cloud.dms.controller.admin.sparepart.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 备品/备件 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SparePartRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14340")
    private String id;

    @Schema(description = "备件编码")
    @ExcelProperty("备件编码")
    private String code;

    @Schema(description = "备件名称", example = "芋艿")
    @ExcelProperty("备件名称")
    private String name;

    @Schema(description = "备件数量")
    @ExcelProperty("备件数量")
    private Integer number;

    @Schema(description = "备件类型", example = "2")
    @ExcelProperty("备件类型")
    private String type;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}