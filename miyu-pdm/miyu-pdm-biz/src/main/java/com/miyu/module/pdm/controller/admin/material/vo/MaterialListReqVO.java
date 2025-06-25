package com.miyu.module.pdm.controller.admin.material.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - PDM 物料列表(包含工装、材料等)-临时 Request VO")
@Data
public class MaterialListReqVO {

    @Schema(description = "物料编号", example = "芋艿")
    private String materialNumber;

    @Schema(description = "物料名称", example = "芋艿")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）", example = "3")
    private String materialProperty;

    @Schema(description = "类型（零件、托盘、工装、夹具、刀具）", example = "3")
    private String materialType;

    @TableField(exist = false)
    @Schema(description = "材料id", example = "20041")
    private String materialId;

    @Schema(description = "材料类码", example = "20041")
    private String materialCode;

    @Schema(description = "材料规格", example = "20041")
    private String materialSpecification;

}
