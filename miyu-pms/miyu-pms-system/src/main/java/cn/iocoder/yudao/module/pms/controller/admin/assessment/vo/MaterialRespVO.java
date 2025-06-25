package cn.iocoder.yudao.module.pms.controller.admin.assessment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "工装RespVO")
@Data
public class MaterialRespVO {

    private String id;
    /**
     * 物料编号
     */
    private String materialNumber;
    /**
     * 物料类码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料规格
     */
    private String materialSpecification;
    /**
     * 物料品牌
     */
    private String materialBrand;
    /**
     * 物料单位
     */
    private String materialUnit;
    /**
     * 物料管理模式
     */
    private Integer materialManage;
    /**
     * 是否质检
     */
    private Integer materialQualityCheck;
    /**
     * 父物料类型
     */
    private String materialParentId;
    /**
     * 物料属性（成品、毛坯、辅助材料）
     */
    private Integer materialProperty;
    /**
     * 物料类型（零件、托盘、工装、夹具、刀具）
     */
    private Integer materialType;
    /**
     * 零件图号
     */
    private String partNumber;

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
