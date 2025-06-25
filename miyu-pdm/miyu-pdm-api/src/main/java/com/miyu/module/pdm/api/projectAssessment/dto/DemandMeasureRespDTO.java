package com.miyu.module.pdm.api.projectAssessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "RPC 服务 - 采购意见-量具")
@Data
public class DemandMeasureRespDTO {

    private String id;

    private String projectCode;

    private String partVersionId;

    private String measureCode;

    private String measureName;

    private String measureSpecification;

    private String description;

    /** 图号 */
    private String partNumber;

    /** 资源类型 */
    private Integer resourcesType;
    /** 资源id */
    private String resourcesTypeId;
    /** 数量 */
    private Integer amount;
//    private Integer quantity;
    /** 预估价格 */
    private BigDecimal predictPrice;
    /** 采购类型，0:已有 1:需采购*/
    private Integer purchaseType;
}
