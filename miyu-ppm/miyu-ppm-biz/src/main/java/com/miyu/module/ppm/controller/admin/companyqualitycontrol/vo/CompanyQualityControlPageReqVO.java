package com.miyu.module.ppm.controller.admin.companyqualitycontrol.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 企业质量控制信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyQualityControlPageReqVO extends PageParam {

    @Schema(description = "企业编号", example = "19277")
    private String companyId;

    @Schema(description = "质量管理体系认证")
    private Integer qmsc;

    @Schema(description = "是否专职检验")
    private Integer inspection;

    @Schema(description = "是否不合格品控制")
    private Integer  nonconformingControl;

    @Schema(description = "生产可追溯")
    private Integer productionTraceability;

    @Schema(description = "是否采购质量控制")
    private Integer purchasingControl;

    @Schema(description = "出厂质量控制")
    private Integer oqc;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}