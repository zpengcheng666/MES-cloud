package com.miyu.module.ppm.controller.admin.shipping.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.miyu.module.ppm.enums.DictTypeConstants.*;

@Schema(description = "管理后台 - 销售发货 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractOrderRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25351")
    private String id;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String projectId;
    @Schema(description = "项目订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String orderId;

    @Schema(description = "物料类型Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialId;

    @Schema(description = "数量", example = "1")
    private BigDecimal quantity;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxRate;

    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxPrice;

    @Schema(description = "交货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime leadDate;

    @Schema(description = "已发数量", example = "1")
    private BigDecimal finishQuantity;

    @Schema(description = "选择数量", example = "1")
    private BigDecimal chooseQuantity;
    @Schema(description = "剩余数量", example = "1")
    private BigDecimal remainingQuantity;
    @Schema(description = "退货数量", example = "1")
    private BigDecimal returnQuantity;
    @Schema(description = "物料编号") 
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;
}