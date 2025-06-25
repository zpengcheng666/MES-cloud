package com.miyu.module.ppm.controller.admin.contract.vo;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 购销合同分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractPageReqVO extends PageParam {

    @Schema(description = "类型(采购、销售)", example = "1")
    private Integer type;

    @Schema(description = "合同编号")
    private String number;

    @Schema(description = "合同名称", example = "李四")
    private String name;

    @Schema(description = "合同方")
    private String party;

    @Schema(description = "签约人")
    private String contact;

    @Schema(description = "签约时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] signingDate;

    @Schema(description = "签约地点")
    private String signingAddress;

    @Schema(description = "签约部门")
    private String department;

    @Schema(description = "我方签约人")
    private String selfContact;

    @Schema(description = "是否增值税")
    private Integer vat;

    @Schema(description = "币种")
    private Integer currency;

    @Schema(description = "交货方式")
    private Integer delivery;

    @Schema(description = "合同状态", example = "1")
    private Integer contractStatus;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "采购员")
    private String purchaser;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    private String contractTypes;
    private Integer contractType;

}