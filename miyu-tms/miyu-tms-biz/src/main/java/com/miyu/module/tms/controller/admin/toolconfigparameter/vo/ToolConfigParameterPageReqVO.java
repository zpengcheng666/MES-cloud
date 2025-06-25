package com.miyu.module.tms.controller.admin.toolconfigparameter.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 刀具参数信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ToolConfigParameterPageReqVO extends PageParam {

    @Schema(description = "刀具类型ID", example = "7161")
    private String toolConfigId;

    @Schema(description = "参数名称", example = "赵六")
    private String name;

    @Schema(description = "参数值")
    private String value;

    @Schema(description = "参数缩写")
    private String abbr;

    @Schema(description = "计量单位")
    private String unit;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "分类 1 几何参数  2 切削参数", example = "1")
    private Boolean type;

}
