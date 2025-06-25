package com.miyu.module.pdm.controller.admin.processSupplement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "pdm - 补加工工艺规程分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessSupplementPageReqVO extends PageParam{

        @Schema(description = "项目号", example = "C")
        private String projectCode;

        @Schema(description = "零件图号", example = "1")
        private String partNumber;

        @Schema(description = "补加工工艺规程编号", example = "1")
        private String processCodeSupplement;

        @Schema(description = "状态", example = "1")
        private String status;
}
