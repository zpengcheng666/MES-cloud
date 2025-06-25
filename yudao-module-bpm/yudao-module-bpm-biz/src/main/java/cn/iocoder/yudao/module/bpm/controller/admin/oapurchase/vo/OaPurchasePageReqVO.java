package cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - OA 采购申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OaPurchasePageReqVO extends PageParam {

    @Schema(description = "申请人部门")
    private String dept;

    @Schema(description = "申请人")
    private String applicant;

    @Schema(description = "采购人")
    private String purchaseAgent;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "供应商编码")
    private String supplyCode;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系电话")
    private String tel;

    @Schema(description = "审批结果", example = "1")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "21999")
    private String processInstanceId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
