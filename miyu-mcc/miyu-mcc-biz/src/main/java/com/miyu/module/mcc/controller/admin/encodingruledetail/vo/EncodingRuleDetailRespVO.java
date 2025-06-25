package com.miyu.module.mcc.controller.admin.encodingruledetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 编码规则配置详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EncodingRuleDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "921")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "类型1机构 2编码分类 3类别4源码5连接符6数字流水号7字母流水号8年份9月份10日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "类型1机构 2编码分类 3类别4源码5连接符6数字流水号7字母流水号8年份9月份10日期", converter = DictConvert.class)
    @DictFormat("encoding_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "编码规则表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23792")
    @ExcelProperty("编码规则表ID")
    private String encodingRuleId;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "位数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("位数")
    private Integer bitNumber;

    @Schema(description = "描述名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("描述名")
    private String name;

    @Schema(description = "默认值")
    @ExcelProperty("默认值")
    private String defalutValue;

    @Schema(description = "规则 1固定  2自定义 3自生成", example = "1")
    @ExcelProperty(value = "规则 1固定  2自定义 3自生成", converter = DictConvert.class)
    @DictFormat("rule_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer ruleType;

    @Schema(description = "编码属性   （当自定义的时候可以选择属性方便传值）")
    @ExcelProperty(value = "编码属性   （当自定义的时候可以选择属性方便传值）", converter = DictConvert.class)
    @DictFormat("encoding_attribute ") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String encodingAttribute;

    @Schema(description = "来源规则Id", example = "9973")
    @ExcelProperty("来源规则Id")
    private String sourceRuleId;


    @Schema(description = "所属分类", example = "9973")
    private String materialTypeId;

}