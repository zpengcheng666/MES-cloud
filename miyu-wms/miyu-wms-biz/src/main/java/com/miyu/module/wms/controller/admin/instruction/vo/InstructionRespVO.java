package com.miyu.module.wms.controller.admin.instruction.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 指令 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InstructionRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7277")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "指令编码")
    @ExcelProperty("指令编码")
    private String insCode;

    @Schema(description = "物料库存id", example = "28973")
    @ExcelProperty("物料库存id")
    private String materialStockId;

    @Schema(description = "指令类型（上架指令、下架指令）", example = "2")
    @ExcelProperty(value = "指令类型（上架指令、下架指令）", converter = DictConvert.class)
    @DictFormat("wms_instruction_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer insType;

    @Schema(description = "指令状态（未开始、进行中、已完成、已取消）", example = "1")
    @ExcelProperty(value = "指令状态（未开始、进行中、已完成、已取消）", converter = DictConvert.class)
    @DictFormat("wms_instruction_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer insStatus;

    @Schema(description = "起始库位id", example = "29629")
    @ExcelProperty("起始库位id")
    private String startLocationId;

    @Schema(description = "目标库位id", example = "14052")
    @ExcelProperty("目标库位id")
    private String targetLocationId;

    @Schema(description = "指令内容")
    @ExcelProperty("指令内容")
    private String insContent;

    @Schema(description = "指令描述", example = "你猜")
    @ExcelProperty("指令描述")
    private String insDescription;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}