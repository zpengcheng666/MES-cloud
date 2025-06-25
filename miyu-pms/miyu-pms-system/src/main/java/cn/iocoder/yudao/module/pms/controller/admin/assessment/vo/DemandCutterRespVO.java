package cn.iocoder.yudao.module.pms.controller.admin.assessment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "采购意见-刀具RespVO")
@Data
public class DemandCutterRespVO {

    private String id;

    private String projectCode;

    private String partVersionId;

    private String length;

    private String bladeLength;

    private String bladeNum;

    private String diameter;

    private String rrAngle;

    private String reducingDiameter;

    private String description;

    /** 图号 */
    private String partNumber;

    /** 资源类型 */
    private Integer resourcesType;
    /** 资源id */
    private String resourcesTypeId;
    /** 数量 */
    private Integer amount;
    /** 采购数量 */
    private Integer purchaseAmount;
//    private Integer quantity;
    /** 预估价格 */
    private BigDecimal predictPrice;
    /** 损耗 */
    private BigDecimal wastage;
    /** 采购类型，0:已有 1:需采购*/
    private Integer purchaseType;
}
