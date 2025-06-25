package com.miyu.module.wms.controller.admin.checkdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 库存盘点详情新增/修改 Request VO")
@Data
public class CheckDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "物料id")
    private String materialStockId;

    @Schema(description = "账面数量")
    private Integer realTotality;

    @Schema(description = "盘点数量")
    private Integer checkTotality;

    @Schema(description = "盘点时间")
    private LocalDateTime checkTime;

    @Schema(description = "盘点容器id")
    private String checkContainerId;

}