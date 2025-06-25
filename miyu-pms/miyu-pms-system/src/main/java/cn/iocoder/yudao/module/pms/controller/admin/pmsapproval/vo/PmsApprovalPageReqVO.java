package cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - pms 立项表,项目立项相关分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PmsApprovalPageReqVO extends PageParam {

    @Schema(description = "项目编码")
    private String projectCode;

    @Schema(description = "项目名称", example = "芋艿")
    private String projectName;

    @Schema(description = "自研/销售 (父部门id)", example = "2")
    private Integer projectType;


    @Schema(description = "项目状态", example = "2")
    private Long projectStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "审批结果", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "1824")
    private String processInstanceId;

    @Schema(description = "项目状态", example = "2")
    private List<Long> projectStatusList;

}
