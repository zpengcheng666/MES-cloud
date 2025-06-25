package cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 项目采购明细,与合同关联,项目执行用
 */
@Data
public class ProjectPurchaseDetailRespVO {

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String contractId;

//    @Schema(description = "物料类型Id", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String materialId;
    /** 数量 */
    private BigDecimal quantity;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

//    @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED)
//    private BigDecimal taxRate;

//    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED)
//    private BigDecimal taxPrice;

    /** 含税总价 */
    private BigDecimal taxTotalPrice;

    @Schema(description = "交货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime leadDate;

    /**
     * 币种
     */
    private Integer currency;

    /**
     * 采购日期
     */
    private LocalDateTime purchaseTime;

    /**
     * 签收数量
     */
    private BigDecimal signAmount;

    /**
     * 剩余数量
     */
    private BigDecimal remainAmount;

    /**
     * 签收(入库)总额
     */
    private BigDecimal signTotalPrice;

    /**
     * 未签收(未入库)总额
     */
    private BigDecimal unSignTotalPrice;

    /**
     * 实际付款
     */
    private BigDecimal payment;
    /**
     * 剩余付款
     */
    private BigDecimal remainPayment;




}
