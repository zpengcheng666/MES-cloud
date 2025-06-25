package com.miyu.module.wms.controller.admin.instruction.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 指令分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InstructionPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "指令编码")
    private String insCode;

    @Schema(description = "物料库存id", example = "28973")
    private String materialStockId;

    @Schema(description = "指令类型（上架指令、下架指令）", example = "2")
    private Integer insType;

    @Schema(description = "指令状态（未开始、进行中、已完成、已取消）", example = "1")
    private Integer insStatus;

    @Schema(description = "起始库位id", example = "29629")
    private String startLocationId;

    @Schema(description = "目标库位id", example = "14052")
    private String targetLocationId;

    @Schema(description = "指令内容")
    private String insContent;

    @Schema(description = "指令描述", example = "你猜")
    private String insDescription;

}