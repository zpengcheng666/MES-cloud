package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 项目零件目录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProjPartBomRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名称")
    private String partName;

    @Schema(description = "零件版次")
    private String partVersion;

    @Schema(description = "产品ID")
    private String rootProductId;

    @Schema(description = "产品图号")
    private String productNumber;

    @Schema(description = "客户化标识")
    private String customizedIndex;

    @Schema(description = "评估者id")
    private String reviewedBy;

    @Schema(description = "评估者姓名")
    private String reviewer;

    @Schema(description = "评估截止日期")
    private String deadline;

    @Schema(description = "状态")
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "评估任务id")
    private String taskId;

    @Schema(description = "流程审批实例id")
    private String processInstanceId;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "评估结果")
    private Integer isPass;

    @Schema(description = "不通过原因")
    private String reason;

    @Schema(description = "加工状态")
    private String processCondition;

    @Schema(description = "技术评估任务id")
    private String feasibilityTaskId;

    @Schema(description = "工艺准备完成时间")
    private String processPreparationTime;

}
