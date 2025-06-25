package com.miyu.module.ppm.controller.admin.dmcontract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;


@Schema(description = "管理后台 - 购销合同 Request VO")
@Data
@ToString(callSuper = true)
public class ContractReqVO {

    @Schema(description = "类型(采购、销售)", example = "1")
    private Integer type;

    @Schema(description = "合同编号")
    private String number;
}