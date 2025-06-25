package com.miyu.module.ppm.api.contract.dto;


import com.fhs.core.trans.vo.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "RPC 服务 - 采购系统 合同 Response DTO")
@Data
public class ContractRespDTO implements VO {
    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25351")
    private String id;

    @Schema(description = "类型(采购、销售)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;

    @Schema(description = "合同名称", example = "李四")
    private String name;

    @Schema(description = "合同方", requiredMode = Schema.RequiredMode.REQUIRED)
    private String party;

    @Schema(description = "签约人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contact;

    @Schema(description = "签约时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime signingDate;

    @Schema(description = "签约地点", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signingAddress;

    @Schema(description = "签约部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String department;

    @Schema(description = "我方签约人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String selfContact;

    @Schema(description = "是否增值税", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer vat;

    @Schema(description = "币种", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer currency;

    @Schema(description = "交货方式", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer delivery;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "采购员", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaser;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "签约部门", example = "你猜")
    private String selfContactName;

    @Schema(description = "我方签约人", example = "你猜")
    private String departmentName;

    @Schema(description = "采购员", example = "你猜")
    private String purchaserName;

    private String projectId;

    @Schema(description = "订单列表")
    private List<ContractOrderDTO> orders;

    @Schema(description = "付款计划列表")
    private List<ContractPaymentSchemeDTO> paymentSchemes;

    @Schema(description = "合同支付")
    private List<ContractPaymentDTO> payments;

    @Schema(description = "合同发票")
    private List<ContractInvoiceDTO> invoices;


}