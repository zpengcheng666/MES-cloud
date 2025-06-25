package com.miyu.cloud.macs.controller.admin.accessApplication.vo;

import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "管理后台 - 通行申请新增/修改 Request VO")
@Data
public class AccessApplicationSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6123")
    private String id;

    @Schema(description = "申请单号")
    private String applicationNumber;

    @Schema(description = "申请代理人")
    private String agent;

    @Schema(description = "公司/组织")
    private String organization;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "申请原因/目的", example = "不香")
    private String reason;

    @Schema(description = "申请状态", example = "1")
    private String status;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "访客区域权限列表")
    private List<VisitorRegionDO> visitorRegions;

}
