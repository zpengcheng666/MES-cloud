package com.miyu.module.mcc.controller.admin.encodingrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;

@Schema(description = "管理后台 - 编码规则配置新增/修改 Request VO")
@Data
public class EncodingRuleSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12881")
    private String id;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "编码分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "28954")
    @NotEmpty(message = "编码分类不能为空")
    private String classificationId;

    @Schema(description = "启用状态  1启用 0未启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "启用状态  1启用 0未启用不能为空")
    private Integer status;

    @Schema(description = "总位数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总位数不能为空")
    private Integer totalBitNumber;

    @Schema(description = "编码规则配置详情列表")
    @NotEmpty(message = "详情不能为空")
    private List<EncodingRuleDetailDO> encodingRuleDetails;

    @Schema(description = "所属分类", example = "9973")
    private String materialTypeId;

    @Schema(description = "类型", example = "9973")
    @NotNull(message = "类型不能为空")
    private Integer encodingRuleType;


    private Integer autoRelease;
}