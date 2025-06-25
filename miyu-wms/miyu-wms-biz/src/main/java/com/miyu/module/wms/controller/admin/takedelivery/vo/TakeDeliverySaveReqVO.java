package com.miyu.module.wms.controller.admin.takedelivery.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 物料收货新增/修改 Request VO")
@Data
public class TakeDeliverySaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15644")
    private String id;

    @Schema(description = "到货单号")
    private String orderNumber;

    @Schema(description = "物料类型id", example = "19072")
    private String materialConfigId;

    @Schema(description = "收货数量")
    private Integer tdQuantity;

    @Schema(description = "绑定库位", example = "17995")
    private String locationId;

    @Schema(description = "绑定储位", example = "9281")
    private String storageId;

    @Schema(description = "绑定物料", example = "17710")
    private String materialId;

}