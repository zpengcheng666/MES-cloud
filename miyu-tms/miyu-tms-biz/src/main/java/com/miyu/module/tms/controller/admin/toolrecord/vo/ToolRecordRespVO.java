package com.miyu.module.tms.controller.admin.toolrecord.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 刀具使用记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolRecordRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "成品刀具id")
    @ExcelProperty("成品刀具id")
    private String toolInfoId;

    @Schema(description = "起始时间")
    @ExcelProperty("起始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "表字段id（目标设备、目标位置、等）")
    @ExcelProperty("表字段id（目标设备、目标位置、等）")
    private String field;

    @Schema(description = "类型（装配、测量、出库、配送、上机、使用、下机、回库、入库、拆卸）")
    @ExcelProperty(value = "类型（装配、测量、出库、配送、上机、使用、下机、回库、入库、拆卸）", converter = DictConvert.class)
    @DictFormat("tool_record_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;


    private String barCode;
    private String materialNumber;
    private String materialName;

}