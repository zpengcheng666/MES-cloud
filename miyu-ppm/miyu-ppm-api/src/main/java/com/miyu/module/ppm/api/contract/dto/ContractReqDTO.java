package com.miyu.module.ppm.api.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 采购系统 合同 Resquest DTO")
@Data
public class ContractReqDTO {

    @Schema(description = "类型(采购、销售)", example = "1")
    private Integer type;

    @Schema(description = "合同编号")
    private String number;

}