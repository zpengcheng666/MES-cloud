package cn.iocoder.yudao.module.pms.controller.admin.order.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 项目订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PmsOrderRespVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25686")
    @ExcelProperty("订单id")
    private String id;

    @Schema(description = "项目编码(本地关联时写入)")
    @ExcelProperty("项目编码(本地关联时写入)")
    private String projectCode;

    @Schema(description = "项目名称")
    @ExcelProperty("项目名称")
    private String projectName;

    @Schema(description = "项目编号", example = "17686")
    @ExcelProperty("项目编号")
    private String projectId;

//    @Schema(description = "合同id", example = "14778")
//    @ExcelProperty("合同id")
//    private String contractId;

    @Schema(description = "物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    @ExcelProperty("物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    private String materialNumber;

    @Schema(description = "图号")
    @ExcelProperty("图号")
    private String partNumber;

    @Schema(description = "工件名称")
    @ExcelProperty("工件名称")
    private String partName;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private Integer quantity;

    @Schema(description = "外协数量")
    @ExcelProperty("外协数量")
    private Integer outSourceAmount;

    @Schema(description = "工序外协")
    private Integer stepOutSourceAmount;

    @Schema(description = "带料加工(是/否)", example = "2")
    @ExcelProperty("带料加工(是/否)")
    private Integer processType;

    @Schema(description = "订单状态(0,未开始,1,待审核,2,准备;3，生产;4,出库;5,关闭)", example = "2")
    @ExcelProperty(value = "订单状态(0,未开始,1,待审核,2,准备;3，生产;4,出库;5,关闭)")
    private Integer orderStatus;

    @Schema(description = "订单类型(0,外部；1内部)", example = "1")
    private Integer orderType;

    @Schema(description = "加工状态", example = "2")
    @ExcelProperty("加工状态")
    private String processCondition;

    @Schema(description = "原料交付时间")
    @ExcelProperty("原料交付时间")
    private LocalDateTime materialDeliveryTime;

    @Schema(description = "成品交付时间")
    @ExcelProperty("成品交付时间")
    private LocalDateTime fproDeliveryTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private Integer materialStatus;
    private Integer productStatus;

    @Schema(description = "整单外协")
    private Integer outsource;

    @Schema(description = "整单外协")
    private Integer outsourcePrepareMaterial;

}
