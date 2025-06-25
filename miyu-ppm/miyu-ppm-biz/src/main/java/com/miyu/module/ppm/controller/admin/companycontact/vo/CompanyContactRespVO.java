package com.miyu.module.ppm.controller.admin.companycontact.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 企业联系人 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CompanyContactRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25056")
    private String id;

    @Schema(description = "企业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4969")
    private String companyId;

    @Schema(description = "公司名称")
    @ExcelProperty("公司名称")
    private String companyName;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("姓名")
    private String name;

    @Schema(description = "部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("部门")
    private String depart;

    @Schema(description = "职务")
    @ExcelProperty("职务")
    private String position;

    @Schema(description = "在职状态：0-在职、1-离职", example = "1")
    @ExcelProperty(value = "在职状态", converter = DictConvert.class)
    @DictFormat("system_job_status")
    private Integer status;

    @Schema(description = "直属上级，子表ID")
    @ExcelProperty("直属上级")
    private String superior;

    @Schema(description = "部门负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "部门负责人", converter = DictConvert.class)
    @DictFormat("pd_header")
    private Integer header;

    @Schema(description = "性别")
    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("system_user_sex")
    private Integer sex;

    @Schema(description = "电话")
    @ExcelProperty("电话")
    private String phone;

    @Schema(description = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Schema(description = "年龄")
    @ExcelProperty("年龄")
    private Integer age;

    @Schema(description = "地址")
    @ExcelProperty("地址")
    private String address;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;


}