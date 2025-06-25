package com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 需求分拣详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchDemandRecordPageReqVO extends PageParam {

    @Schema(description = "物料id", example = "3360")
    private String materialUid;

    @Schema(description = "批次id", example = "3360")
    private String batchId;

    @Schema(description = "批次任务id")
    private String batchRecordId;

    @Schema(description = "需求id", example = "3360")
    private String demandId;

    @Schema(description = "物料类型id", example = "27367")
    private String materialConfigId;

    @Schema(description = "物料类型编号")
    private String materialNumber;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次")
    private String batchNumber;

    @Schema(description = "数量")
    private Integer totality;

    @Schema(description = "是否为批量")
    private Boolean batch;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "配送状态")
    private Integer status;

}
