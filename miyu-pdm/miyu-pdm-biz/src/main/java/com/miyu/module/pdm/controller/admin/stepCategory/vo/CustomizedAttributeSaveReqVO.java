package com.miyu.module.pdm.controller.admin.stepCategory.vo;

import com.miyu.module.pdm.dal.dataobject.stepCategory.CustomizedAttributeDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PDM 自定义属性 Request VO")
@Data
public class CustomizedAttributeSaveReqVO extends CustomizedAttributeDO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5860")
    private String id;

    @Schema(description = "自定义属性对应的类别类别", requiredMode = Schema.RequiredMode.REQUIRED, example = "21829")
    private String categoryId;

    @Schema(description = "自定义属性中文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private String attrNameCn;

    @Schema(description = "自定义属性英文名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private String attrNameEn;

    @Schema(description = "自定义属性类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private Integer attrType;

    @Schema(description = "计量单位", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private String attrUnit;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private Integer attrOrder;

    @Schema(description = "默认值", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private String attrDefaultValue;

    @Schema(description = "是否多值", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private Integer isMultvalues;

    @Schema(description = "有效值数组", requiredMode = Schema.RequiredMode.REQUIRED, example = "S110")
    private String attrEffectiveValue;
}
