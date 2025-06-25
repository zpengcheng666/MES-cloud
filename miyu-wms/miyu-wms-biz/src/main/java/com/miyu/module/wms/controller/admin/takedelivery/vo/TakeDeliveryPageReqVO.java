package com.miyu.module.wms.controller.admin.takedelivery.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料收货分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TakeDeliveryPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "到货单号")
    private String orderNumber;

    @Schema(description = "物料类型id", example = "19072")
    private String materialConfigId;

    @Schema(description = "绑定库位", example = "17995")
    private String locationId;

    @Schema(description = "绑定储位", example = "9281")
    private String storageId;

    @Schema(description = "绑定物料", example = "17710")
    private String materialId;

}