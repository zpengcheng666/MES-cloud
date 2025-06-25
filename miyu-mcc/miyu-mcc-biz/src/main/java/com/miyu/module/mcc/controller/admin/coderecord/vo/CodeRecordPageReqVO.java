package com.miyu.module.mcc.controller.admin.coderecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 编码记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CodeRecordPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "赵六")
    private String name;

    @Schema(description = "父类型id", example = "16564")
    private String parentId;

    @Schema(description = "状态  1 预生成 2 已使用  3释放", example = "2")
    private Integer status;

    @Schema(description = "编码规则ID", example = "15408")
    private String encodingRuleId;

    @Schema(description = "编码分类ID", example = "3088")
    private String classificationId;


}