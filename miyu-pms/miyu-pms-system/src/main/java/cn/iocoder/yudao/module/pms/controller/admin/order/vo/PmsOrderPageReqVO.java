package cn.iocoder.yudao.module.pms.controller.admin.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PmsOrderPageReqVO extends PageParam {

    @Schema(description = "项目编码(本地关联时写入)")
    private String projectCode;

    @Schema(description = "项目id(本地关联时写入)", example = "13694")
    private String projectId;

    @Schema(description = "物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    private String materialNumber;

    @Schema(description = "图号")
    private String partNumber;

    @Schema(description = "工件名称")
    private String partName;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "外协数量")
    private Integer outSourceAmount;

    @Schema(description = "工序外协")
    private Integer stepOutSourceAmount;

    @Schema(description = "带料加工(是/否)", example = "2")
    private Integer processType;

    @Schema(description = "订单状态(0,未开始,1,待审核,2,准备;3，生产;4,出库;5,关闭)", example = "2")
    private Integer orderStatus;

    @Schema(description = "订单类型(0,外部；1内部)", example = "1")
    private Integer orderType;

    @Schema(description = "加工状态")
    private String processCondition;

    @Schema(description = "原料交付时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] materialDeliveryTime;

    @Schema(description = "成品交付时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] fproDeliveryTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "整单外协")
    private Integer outsource;

    @Schema(description = "整单外协备料")
    private Integer outsourcePrepareMaterial;

}
