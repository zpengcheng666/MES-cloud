package cn.iocoder.yudao.module.pms.controller.admin.order.vo;

import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 项目订单新增/修改 Request VO")
@Data
public class PmsOrderSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32706")
    private String id;

    @Schema(description = "项目编码(本地关联时写入)")
    private String projectCode;

    @Schema(description = "项目id(本地关联时写入)", example = "13694")
    private String projectId;

    @Schema(description = "项目名称", example = "13694")
    private String projectName;

    @Schema(description = "物料牌号")
    private String material;

    @Schema(description = "物料编号,代表物料类型")
    private String materialNumber;

    @Schema(description = "图号")
    private String partNumber;

    @Schema(description = "工件名称")
    @ExcelProperty("工件名称")
    private String partName;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "带料加工(是/否)", example = "2")
    private Integer processType;

    @Schema(description = "订单状态(1,准备；2，生产；3,出库；4，关闭)", example = "2")
    private Integer orderStatus;

    @Schema(description = "订单类型(0,外部；1内部)", example = "1")
    private Integer orderType;

    @Schema(description = "外协数量")
    @ExcelProperty("外协数量")
    private Integer outSourceAmount;

    @Schema(description = "工序外协")
    private Integer stepOutSourceAmount;

    @Schema(description = "加工状态")
    private String processCondition;

    @Schema(description = "物料状态,0为存在,1为不存在")
    private String materialStatus;
    @Schema(description = "产品状态,0为存在,1为不存在")
    private String productStatus;

    @Schema(description = "原料交付时间")
    private LocalDateTime materialDeliveryTime;

    @Schema(description = "成品交付时间")
    private LocalDateTime fproDeliveryTime;

    @Schema(description = "整单外协")
    private Integer outsource;

    @Schema(description = "整单外协")
    private Integer outsourcePrepareMaterial;

}
