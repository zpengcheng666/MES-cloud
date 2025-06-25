package com.miyu.module.qms.controller.admin.managementdatabase.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 质量管理资料库分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ManagementDatabasePageReqVO extends PageParam {

    @Schema(description = "所属质量管理关联ID", example = "25724")
    private String treeId;

    @Schema(description = "类型", example = "2")
    private Integer type;

    @Schema(description = "附件", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "工作流编号", example = "14023")
    private String processInstanceId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}