package com.miyu.cloud.mcs.controller.admin.receiptrecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 生产单元签收记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiptRecordPageReqVO extends PageParam {

    @Schema(description = "申请id", example = "7789")
    private String applicationId;

    @Schema(description = "申请编号")
    private String applicationNumber;

    @Schema(description = "配送数量")
    private String count;

    @Schema(description = "需求详情id")
    private String demandRecordId;

    @Schema(description = "物料id")
    private String materialUid;

    @Schema(description = "物料批次编码")
    private String batchNumber;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "申请单元", example = "242")
    private String processingUnitId;

    private String unitName;

    @Schema(description = "资源类型", example = "2")
    private Integer resourceCode;

    @Schema(description = "资源类码")
    private String resourceTypeCode;

    private Integer pageBegin;

}
