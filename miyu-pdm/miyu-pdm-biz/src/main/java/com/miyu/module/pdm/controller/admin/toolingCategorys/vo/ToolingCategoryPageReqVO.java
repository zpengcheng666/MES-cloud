package com.miyu.module.pdm.controller.admin.toolingCategorys.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产品分类信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToolingCategoryPageReqVO extends PageParam {

    @Schema(description = "父分类编号", example = "17072")
    private Long parentId;

    @Schema(description = "分类名称", example = "王五")
    private String name;

    @Schema(description = "分类编码")
    private String code;

    @Schema(description = "分类排序")
    private Integer sort;

    @Schema(description = "开启状态（0正常 1停用）", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}