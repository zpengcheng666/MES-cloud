package cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 订单物料关系表新增/修改 Request VO")
@Data
public class OrderMaterialRelationSaveReqVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22368")
    private String id;

    @Schema(description = "项目id(本地关联时写入)", example = "31536")
    private String orderId;

    @Schema(description = "物料编码，牌号，毛坯(本地关联时写入)")
    private String materialCode;

    @Schema(description = "物料类型编号")
    private String materialTypeId;

    @Schema(description = "变码，工序加工后，产生的新码")
    private String variableCode;

    @Schema(description = "图号(成品工件)")
    private String productCode;

    @Schema(description = "项目id", example = "29810")
    private String projectId;

    @Schema(description = "计划id", example = "24514")
    private String planId;

    @Schema(description = "子计划id", example = "647")
    private String planItemId;

    @Schema(description = "计划类型")
    private Integer planType;

    @Schema(description = "物料状态", example = "647")
    private Integer materialStatus;

    @Schema(description = "是否备料")
    private boolean prepare;

    @Schema(description = "备料选择的物料码")
    private List<String> materialCodeList;

    @Schema(description = "工序")
    private String step;

    @Schema(description = "工艺方案,非表中字段")
    private String processScheme;

}
