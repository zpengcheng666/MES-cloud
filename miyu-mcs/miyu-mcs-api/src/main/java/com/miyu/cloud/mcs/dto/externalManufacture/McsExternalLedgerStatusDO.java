package com.miyu.cloud.mcs.dto.externalManufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class McsExternalLedgerStatusDO {

    //id
    @Schema(description = "设备id(id与编码必填一个)", example = "123456")
    private String id;
    //设备/工位编号
    @Schema(description = "设备编码(id与编码必填一个)", example = "abc")
    private String code;
    //状态
    @Schema(description = "设备状态(0:正常;1:禁用;2:维修)", example = "1")
    private Integer status;
    //运行状态
    @Schema(description = "设备状态(0:加工中;1:空闲;2:停机;3:暂停;4:急停;5:报警)", example = "1")
    private Integer runStatus;
    //在线状态
    @Schema(description = "设备状态(0:在线;1:半离线;2:离线)", example = "1")
    private Integer onlineStatus;
    //是否可配送料
    @Schema(description = "是否可配送物料", example = "false")
    private Boolean deliverable;
}
