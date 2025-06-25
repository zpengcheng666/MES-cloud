package com.miyu.module.qms.controller.admin.managementtree.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 质量管理关联树分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ManagementTreePageReqVO extends PageParam {

    @Schema(description = "父节点")
    private String parent;

    @Schema(description = "节点关联字段ID", example = "1234")
    private String nodeId;

    @Schema(description = "节点名", example = "李四")
    private String nodeName;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}