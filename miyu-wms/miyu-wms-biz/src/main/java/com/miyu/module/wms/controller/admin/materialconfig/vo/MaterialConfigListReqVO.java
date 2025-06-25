package com.miyu.module.wms.controller.admin.materialconfig.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料类型列表 Request VO")
@Data
public class MaterialConfigListReqVO {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

   /* @Schema(description = "物料属性（成品、毛坯、辅助材料）")
    private Integer materialProperty;*/

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）", example = "1")
    private String materialType;

    @Schema(description = "物料管理模式")
    private Integer materialManage;

    @Schema(description = "出库规则")
    private Integer materialOutRule;

    @Schema(description = "是否单储位")
    private Integer materialStorage;

    @Schema(description = "是否为容器")
    private Integer materialContainer;

    @Schema(description = "是否质检")
    private Integer materialQualityCheck;

    @Schema(description = "存放指定容器")
    private List<String> containerConfigIds;

    @Schema(description = "默认存放仓库")
    private String defaultWarehouseId;

    @Schema(description = "预警库存")
    private Integer warningStock;

}