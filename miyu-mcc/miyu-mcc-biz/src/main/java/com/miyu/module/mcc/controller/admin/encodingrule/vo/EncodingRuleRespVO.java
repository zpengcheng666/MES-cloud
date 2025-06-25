package com.miyu.module.mcc.controller.admin.encodingrule.vo;

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

@Schema(description = "管理后台 - 编码规则配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EncodingRuleRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12881")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "编码分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "28954")
    @ExcelProperty("编码分类")
    private String classificationId;

    @Schema(description = "启用状态  1启用 0未启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "启用状态  1启用 0未启用", converter = DictConvert.class)
    @DictFormat("mcc_enable_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "总位数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总位数")
    private Integer totalBitNumber;



    @Schema(description = "所属分类", example = "9973")
    private String materialTypeId;

    @Schema(description = "类型", example = "9973")
    private Integer encodingRuleType;


    private String classificationName;

    private String classificationCode;
    /***
     * 所属类别（树形结构）
     */

    private String materialTypeCode;
    private String materialTypeName;

    private Integer autoRelease;
}