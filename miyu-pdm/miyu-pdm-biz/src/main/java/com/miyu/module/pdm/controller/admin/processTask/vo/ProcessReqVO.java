package com.miyu.module.pdm.controller.admin.processTask.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Schema(description = "管理后台 - 工艺任务项目列表 Request VO")
@Data
@ToString(callSuper = true)
public class ProcessReqVO {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id", example = "20041")
    private String id;

    @Schema(description = "零部件版本id", example = "20041")
    private String partVersionId;

    @Schema(description = "零件工艺规程编号", example = "20041")
    private String processCode;

    @Schema(description = "加工方案码", example = "20041")
    private String processSchemeCode;

    @Schema(description = "材料id", example = "20041")
    private String materialId;

    @Schema(description = "材料牌号", example = "20041")
    private String materialDesg;

    @Schema(description = "材料类码", example = "20041")
    private String materialCode;

    @Schema(description = "材料名称", example = "20041")
    private String materialName;

    @Schema(description = "材料状态", example = "20041")
    private String materialCondition;

    @Schema(description = "材料标准", example = "20041")
    private String materialSpec;

    @Schema(description = "材料规格", example = "20041")
    private String materialSpecification;

    @Schema(description = "单件毛料尺寸", example = "20041")
    private String singleSize;

    @Schema(description = "成组尺寸", example = "20041")
    private String groupSize;

    @Schema(description = "加工路线", example = "20041")
    private String processRouteName;

    @Schema(description = "单机数量", example = "20041")
    private Integer singleQuatity;

    @Schema(description = "是否有效", example = "20041")
    private Integer isValid;

    @TableField(exist = false)
    private String processName;

    @TableField(exist = false)
    private String processVersion;

    @TableField(exist = false)
    private String description;

    @TableField(exist = false)
    private String processRegulations;

    @TableField(exist = false)
    private String partNumber;

}

