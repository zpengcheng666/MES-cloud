package cn.iocoder.yudao.module.pms.controller.admin.assessment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;


@Schema(description = "设备RespVO")
@Data
public class DeviceRespVO {

    private String id;

    private String code;

    private String name;

    private String specification;

    private String manufacturer;

    private String countryRegion;

    private String contacts;

    private String contactPhone;

    private String remark;

    private String partNumber;

    private BigDecimal processingTime;

    /** 资源类型 */
    private Integer resourcesType;
    /** 资源id */
    private String resourcesTypeId;
    /** 数量 */
    private Integer amount;
//    private Integer quantity;
    /** 采购数量 */
    private Integer purchaseAmount;
    /** 预估价格 */
    private BigDecimal predictPrice;
    /** 损耗 */
    private BigDecimal wastage;
    /** 采购类型，0:已有 1:需采购*/
    private Integer purchaseType;



}
