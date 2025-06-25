package com.miyu.module.ppm.controller.admin.companycontact.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 企业联系人分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyContactPageReqVO extends PageParam {

    @Schema(description = "企业ID", example = "4969")
    private String companyId;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "部门")
    private String depart;

    @Schema(description = "职务")
    private String position;

    @Schema(description = "在职状态：0-在职、1-离职", example = "1")
    private Integer status;

    @Schema(description = "直属上级，子表ID")
    private String superior;

    @Schema(description = "部门负责人")
    private Integer header;

    @Schema(description = "性别")
    private Integer sex;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}