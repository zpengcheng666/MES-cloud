package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;


import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Schema(description = "管理后台 - 项目编号列表 Request VO")
@Data
@ToString(callSuper = true)
public class ProcessPlanDetailReqVO {
    @Schema(description = "id", example = "20041")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零件号")
    private String partVersionId;

    @Schema(description = "工艺规程版次号")
    private String processVersionId;

    @Schema(description = "零件图号", example = "A220")
    private String partNumber;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "工序名称")
    private String procedureName;

    //工序
    @Schema(description = "工序序号")
    private String procedureNum;

    @Schema(description = "加工工时")
    private Integer processingTime;

    @Schema(description = "准备工时")
    private String preparationTime;

    @Schema(description = "是否检验")
    private String isInspect;

    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    @Schema(description = "检验类型数组：0自检、1互检、2专检")
    private Set<Integer> inspectType;

    @Schema(description = "工序关重属性")
    private String procedureProperty;
    //工序新增字段
    @Schema(description = "是否外委")
    private Integer isOut;

    @Schema(description = "工作说明(html)-预览")
    private String descriptionPreview;

    //工步
    @Schema(description = "工步关联工序ID")
    private String procedureId;

    @Schema(description = "工步描述")
    private String description;

    @Schema(description = "工步名称")
    private String stepName;

    @Schema(description = "工步号")
    private String stepNum;

    @Schema(description = "关重属性")
    private String stepProperty;

    @Schema(description = "工步类型")
    private Long categoryId;

    @Schema(description = "工步自定义属性")
    private List<CustomizedAttributeValReqVO> attributeValList;

    private Set<Integer> slotNumber;

    private String furnaceTemperature;

    private String heatTemperature;

    private String heatUpTime;

    private String keepHeatTime;

    private String coolingMedium;

    private String coolingTemperature;

    private String coolingTime;

}
