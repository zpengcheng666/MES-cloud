package com.miyu.module.tms.controller.admin.toolinfo.vo;

import com.alibaba.fastjson.JSONArray;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 - 刀组信息新增/修改 Request VO")
@Data
public class ToolInfoSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "物料id")
    private String materialStockId;

    @Schema(description = "物料类型id（冗余）")
    private String materialConfigId;

    @Schema(description = "状态(1，可用，2不可用)")
    private Integer status;

    @Schema(description = "刀具装配任务id", example = "10627")
    private String assembleTaskId;
    @Schema(description = "位置ID", example = "10627")
    private String storageId;

    @Schema(description = "刀柄信息")
    private List<AssembleRecordVO>  toolHandleList;
    @Schema(description = "刀具信息")
    private List<AssembleRecordVO>  toolHeadList;
    @Schema(description = "配件")
    private List<AssembleRecordVO>  toolAccessoryList;

    @Schema(description = "移除的装配信息")
    private List<AssembleRecordVO>  removeList;

    //保存类型  1 保存  2提交
    private Integer saveType;

    //类型  1装刀  2 卸刀 3 维修

    private Integer type;
}