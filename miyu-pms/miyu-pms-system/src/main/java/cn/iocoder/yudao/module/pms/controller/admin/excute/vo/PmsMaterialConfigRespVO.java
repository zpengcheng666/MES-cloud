package cn.iocoder.yudao.module.pms.controller.admin.excute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 项目执行材料返回")
@Data
public class PmsMaterialConfigRespVO {
    @Schema(description = "物料类型 ID")
    private String id;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）")
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）")
    private Integer materialType;

    @Schema(description = "物料管理模式")
    private Integer materialManage;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    /** 计划数量 */
    private String q;

    /** 单据编号(合同id) */
    private String contractId;
}
