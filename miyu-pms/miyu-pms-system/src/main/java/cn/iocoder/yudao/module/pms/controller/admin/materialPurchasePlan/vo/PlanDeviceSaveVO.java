package cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 物料采购计划-设备")
@Data
public class PlanDeviceSaveVO {

    /**
     * 项目计划id
     */
    private String projectPlanId;
    /**
     * 项目计划子计划id
     */
    private String projectPlanItemId;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 项目编号
     */
    private String ProjectCode;
    /**
     * 设备类型
     */
    private String type;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 规格
     */
    private String specification;
    /**
     * 工序名
     */
    private String procedureName;
    /**
     * 使用数量
     */
    private String useInventory;
}
