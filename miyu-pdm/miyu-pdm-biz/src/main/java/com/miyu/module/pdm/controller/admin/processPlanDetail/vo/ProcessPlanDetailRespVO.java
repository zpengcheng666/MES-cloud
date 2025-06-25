package com.miyu.module.pdm.controller.admin.processPlanDetail.vo;

import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目零件目录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessPlanDetailRespVO {
    @Schema(description = "工艺规程编号", example = "123")
    private String id;

    @Schema(description = "工步序号", example = "123")
    private String stepNum;

    @Schema(description = "加工方案码", example = "123")
    private String processSchemeCode;

    @Schema(description = "工艺规程ID", example = "123")
    private String processId;

    @Schema(description = "工艺规程编号", example = "123")
    private String processCode;

    @Schema(description = "工艺规程名称")
    private String processName;

    @Schema(description = "检验类型数组：0自检、1互检、2专检")
    private Set<Long> inspectType;

    @Schema(description = "版次")
    private String processVersion;

    @Schema(description = "版次ID")
    private String processVersionId;

    @Schema(description = "流程审批实例id")
    private String processInstanceId;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "关重属性")
    private Integer property;

    @Schema(description = "工艺规程名称")
    private String productNumber;

    @Schema(description = "版次")
    private String rootProductId;

    @Schema(description = "版次")
    private String customizedIndex;

    @Schema(description = "负责人id")
    private String reviewedBy;

    @Schema(description = "负责人姓名")
    private String reviewer;

    @Schema(description = "截止日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private LocalDateTime deadline;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @Schema(description = "零件编号")
    private String partVersionId;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名")
    private String partName;

    @Schema(description = "零件版本")
    private String partVersion;

    @Schema(description = "加工状态")
    private String processCondition;

    @Schema(description = "零件图号")
    private Integer type;

    @Schema(description = "工序ID")
    private String parentId;

    @Schema(description = "零件图号")
    private String name;

    @Schema(description = "工步ID")
    private String serialnum;

    @Schema(description = "材料ID")
    private String materialId;

    @Schema(description = "材料编号")
    private String materialNumber;

    @Schema(description = "材料牌号")
    private String materialDesg;

    @Schema(description = "材料类码")
    private String materialCode;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材料状态")
    private String materialCondition;

    @Schema(description = "材料标准")
    private String materialSpec;

    @Schema(description = "材料规格")
    private String materialSpecification;

    @Schema(description = "单件毛料尺寸")
    private String singleSize;

    @Schema(description = "成组加工尺寸")
    private String groupSize;

    @Schema(description = "加工路线")
    private String processRouteName;

    @Schema(description = "单机数量")
    private Integer singleQuatity;

    //工序
    @Schema(description = "工序序号")
    private String procedureNum;

    @Schema(description = "关重属性")
    private Integer procedureProperty;

    @Schema(description = "工序名称")
    private String procedureName;

    @Schema(description = "是否检验")
    private Integer isInspect;

    @Schema(description = "准备工时")
    private String preparationTime;

    @Schema(description = "加工工时")
    private Integer processingTime;

    @Schema(description = "工序工作说明")
    private String description;
    //工序新增字段
    @Schema(description = "是否外委")
    private Integer isOut;

    //工步
    @Schema(description = "工步关联工序ID")
    private String procedureId;

    @Schema(description = "工步名称")
    private String stepName;

    @Schema(description = "关重属性")
    private Integer stepProperty;

    @Schema(description = "工步工作描述")
    private String stepDescription;

    @Schema(description = "刀简号")
    private String cutternum;

    private Set<String> slotNumber;

    private String furnaceTemperature;

    private String heatTemperature;

    private String heatUpTime;

    private String keepHeatTime;

    private String coolingMedium;

    private String coolingTemperature;

    private String coolingTime;


}
