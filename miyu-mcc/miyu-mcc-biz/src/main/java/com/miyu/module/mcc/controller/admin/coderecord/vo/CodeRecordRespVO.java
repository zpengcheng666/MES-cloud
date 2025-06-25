package com.miyu.module.mcc.controller.admin.coderecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 编码记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CodeRecordRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3984")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "名称", example = "赵六")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "父类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16564")
    @ExcelProperty("父类型id")
    private String parentId;

    @Schema(description = "状态  1 预生成 2 已使用  3释放", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态  1 预生成 2 已使用  3释放", converter = DictConvert.class)
    @DictFormat("mcc_record_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "编码规则ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15408")
    @ExcelProperty("编码规则ID")
    private String encodingRuleId;

    @Schema(description = "编码分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3088")
    @ExcelProperty("编码分类ID")
    private String classificationId;


    @Schema(description = "编码规则", example = "15408")
    @ExcelProperty("编码规则")
    private String encodingRuleName;
    @Schema(description = "编码分类", example = "3088")
    @ExcelProperty("编码分类")
    private String classificationName;
    @ExcelProperty("编码分类码")
    private String classificationCode;
}