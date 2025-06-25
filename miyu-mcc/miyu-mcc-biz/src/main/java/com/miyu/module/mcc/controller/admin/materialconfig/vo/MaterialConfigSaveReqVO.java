package com.miyu.module.mcc.controller.admin.materialconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 物料类型新增/修改 Request VO")
@Data
public class MaterialConfigSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23213")
    private String id;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料名称", example = "王五")
    private String materialName;

    @Schema(description = "当前类别", example = "8228")
    private String materialTypeId;

    @Schema(description = "主类别（工件、托盘、工装、夹具、刀具）", example = "22245")
    private String materialParentTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "来源物料类型", example = "31201")
    private String materialSourceId;

    @Schema(description = "类码")
    private String materialTypeCode;
    @Schema(description = "物料管理模式")
    private Integer materialManage;
}