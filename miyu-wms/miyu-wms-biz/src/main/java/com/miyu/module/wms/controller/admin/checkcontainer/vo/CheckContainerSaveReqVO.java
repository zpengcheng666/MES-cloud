package com.miyu.module.wms.controller.admin.checkcontainer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库存盘点容器新增/修改 Request VO")
@Data
public class CheckContainerSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "盘点计划id")
    private String checkPlanId;

    @Schema(description = "容器库存id")
    private String containerStockId;

    @Schema(description = "盘点状态")
    private Integer checkStatus;

}