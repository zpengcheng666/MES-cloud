package com.miyu.module.tms.controller.admin.toolparamtemplate.vo;

import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 刀具参数模板 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolParamTemplateRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5139")
    private String id;

    @Schema(description = "模板名称", example = "张三")
    @ExcelProperty(value = "模板名称", order = 1)
    private String templateName;

    @Schema(description = "版本号， 每次更新递增", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "版本号", order = 5)
    private Integer version;

    @Schema(description = "状态(0:失效 1:有效)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "创建时间", order = 6)
    private LocalDateTime createTime;

    @Schema(description = "物料类别ID", example = "1")
    private String materialTypeId;

    @Schema(description = "刀具类型ID", example = "1")
    private String toolConfigId;

    @Schema(description = "物料编号",  example = "1")
    private String toolNumber;

    @Schema(description = "物料类码",  example = "1")
    private String toolTypeCode;

    @Schema(description = "物料类别名称", example = "1")
    @ExcelProperty(value = "物料编号", order = 2)
    private String materialTypeName;

    /**
     * 参数模版详情列表
     */
    private List<ToolParamTemplateDetailDO> detailList;


    /**
     * 刀具适配集合
     */
    private List<FitConfigDO> fitConfigList;
}
