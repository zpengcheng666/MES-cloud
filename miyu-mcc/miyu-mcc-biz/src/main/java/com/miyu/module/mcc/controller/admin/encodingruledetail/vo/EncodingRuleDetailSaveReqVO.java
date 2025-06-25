package com.miyu.module.mcc.controller.admin.encodingruledetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 编码规则配置详情新增/修改 Request VO")
@Data
public class EncodingRuleDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "921")
    private String id;

    @Schema(description = "类型1机构 2编码分类 3类别4源码5连接符6数字流水号7字母流水号8年份9月份10日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "类型1机构 2编码分类 3类别4源码5连接符6数字流水号7字母流水号8年份9月份10日期不能为空")
    private Integer type;

    @Schema(description = "编码规则表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23792")
    @NotEmpty(message = "编码规则表ID不能为空")
    private String encodingRuleId;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "位数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "位数不能为空")
    private Integer bitNumber;

    @Schema(description = "描述名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "描述名不能为空")
    private String name;

    @Schema(description = "默认值")
    private String defalutValue;

    @Schema(description = "规则 1固定  2自定义 3自生成", example = "1")
    private Integer ruleType;

    @Schema(description = "编码属性   （当自定义的时候可以选择属性方便传值）")
    private String encodingAttribute;

    @Schema(description = "来源规则Id", example = "9973")
    private String sourceRuleId;

    @Schema(description = "所属分类", example = "9973")
    private String materialTypeId;

}