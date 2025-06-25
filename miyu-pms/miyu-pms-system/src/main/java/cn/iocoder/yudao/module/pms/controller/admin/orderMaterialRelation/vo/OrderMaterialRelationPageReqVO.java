package cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 订单物料关系表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderMaterialRelationPageReqVO extends PageParam {

    @Schema(description = "项目id(本地关联时写入)", example = "31536")
    private String orderId;

    @Schema(description = "物料编码，牌号，毛坯(本地关联时写入)")
    private String materialCode;

    @Schema(description = "物料类型id")
    private String materialTypeId;;

    @Schema(description = "变码，工序加工后，产生的新码")
    private String variableCode;

    @Schema(description = "图号(成品工件)")
    private String productCode;

    @Schema(description = "项目id", example = "29810")
    private String projectId;

    @Schema(description = "计划id", example = "24514")
    private String planId;

    @Schema(description = "子计划id", example = "647")
    private String planItemId;

    @Schema(description = "物料状态", example = "647")
    private Integer materialStatus;

    @Schema(description = "计划类型")
    private Integer planType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "是否备料")
    private boolean prepare;

    @Schema(description = "工序")
    private Integer step;

    @Schema(description = "订单编码")
    private String orderNumber;

}
