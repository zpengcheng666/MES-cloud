package com.miyu.module.pdm.controller.admin.dataStatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 数据包管理 Request VO")
@Data
@ToString(callSuper = true)
public class BomReqVO {

    @Schema(description = "数据包接收id", example = "20041")
    private String receiveInfoId;

    @Schema(description = "零件图号", example = "A220")
    private String partNumber;
}
