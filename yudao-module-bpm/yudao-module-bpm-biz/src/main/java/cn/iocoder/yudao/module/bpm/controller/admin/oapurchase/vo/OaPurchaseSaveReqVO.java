package cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo;

import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseListDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - OA 采购申请新增/修改 Request VO")
@Data
public class OaPurchaseSaveReqVO {

    @Schema(description = "请假表单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26216")
    private Long id;

    @Schema(description = "申请人部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请人部门不能为空")
    private String dept;

    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请人不能为空")
    private String applicant;

    @Schema(description = "采购人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "采购人不能为空")
    private String purchaseAgent;

    @Schema(description = "供应商", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "供应商不能为空")
    private String supplier;

    @Schema(description = "供应商编码")
    private String supplyCode;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系电话")
    private String tel;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
//    @NotNull(message = "审批结果不能为空")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "21999")
    private String processInstanceId;

    @Schema(description = "OA 采购申请列表")
    private List<OaPurchaseListDO> oaPurchaseLists;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

}
