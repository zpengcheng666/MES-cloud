package com.miyu.module.wms.api.mateiral.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - 物料类型新增/修改 Request VO")
@Data
public class MaterialConfigSaveReqDTO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1364")
    private String id;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

/*    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;*/

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialType;
    private String materialTypeName;

    @Schema(description = "托盘类型",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer containerType;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "物料管理模式")
    private Integer materialManage;

    @Schema(description = "出库规则")
    private Integer materialOutRule;

    @Schema(description = "是否单储位")
    private Integer materialStorage;

    @Schema(description = "是否为容器")
    private Integer materialContainer;

    @Schema(description = "是否质检")
    private Integer materialQualityCheck;

    @Schema(description = "存放指定容器")
    private List<String> containerConfigIds;

    @Schema(description = "层")
    private Integer materialLayer;

    @Schema(description = "排")
    private Integer materialRow;

    @Schema(description = "列")
    private Integer materialCol;

    @Schema(description = "默认存放仓库")
    @NotEmpty(message = "必须指定默认存放仓库")
    private String defaultWarehouseId;

    private String areaCode;

    private String materialNumberParent;


    @Schema(description = "来源物料类型")
    private String materialSourceId;

    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
}
