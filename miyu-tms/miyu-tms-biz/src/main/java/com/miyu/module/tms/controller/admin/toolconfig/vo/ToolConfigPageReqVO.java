package com.miyu.module.tms.controller.admin.toolconfig.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 刀具类型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToolConfigPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "名称")
    private String toolName;

    @Schema(description = "型号")
    private String toolModel;

    @Schema(description = "重量")
    private Double toolWeight;

    @Schema(description = "材质")
    private String toolTexture;

    @Schema(description = "涂层")
    private String toolCoating;

    @Schema(description = "额定寿命")
    private BigDecimal ratedLife;

    @Schema(description = "刀组状态0停用1正常")
    private Boolean status;

    @Schema(description = "刀具类型")
    private String toolType;

    @Schema(description = "物料类别ID")
    private String materialTypeId;

    @Schema(description = "物料类码")
    private String materialTypeCode;

    @Schema(description = "物料类别名称")
    private String materialTypeName;


    /**
     * 查询类型  1 刀头  2 刀柄和配件
     */
    private Integer queryType;


    @Schema(description = "刀具类型id集合", example = "3274")
    private String ids;

    @Schema(description = "适配刀具类型id集合", example = "3274")
    private List<String> fitConfigIds;
}
