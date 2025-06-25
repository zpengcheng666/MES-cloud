package cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo;

import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyListDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - OA 物品领用新增/修改 Request VO")
@Data
public class OaSupplySaveReqVO {

    @Schema(description = "请假表单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "560")
    private Long id;

    @Schema(description = "申请人部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请人部门不能为空")
    private String dept;

    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请人不能为空")
    private String applicant;

    @Schema(description = "申请部门领导", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请部门领导不能为空")
    private String leader;

    @Schema(description = "仓库管理员")
    private String warehouseman;

    @Schema(description = "申请原因", example = "不好")
    private String reason;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
//    @NotNull(message = "审批结果不能为空")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "28355")
    private String processInstanceId;

    @Schema(description = "OA 物品领用表-物品清单列表")
    private List<OaSupplyListDO> oaSupplyLists;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

}
