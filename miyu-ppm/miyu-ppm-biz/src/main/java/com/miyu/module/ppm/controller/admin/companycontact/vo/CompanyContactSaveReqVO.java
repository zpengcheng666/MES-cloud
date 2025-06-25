package com.miyu.module.ppm.controller.admin.companycontact.vo;

import cn.iocoder.yudao.framework.common.validation.Telephone;
import com.miyu.module.ppm.framework.operatelog.core.company.SexParseFunction;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 企业联系人新增/修改 Request VO")
@Data
public class CompanyContactSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25056")
    private String id;

    @Schema(description = "企业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4969")
    @NotEmpty(message = "企业ID不能为空")
    private String companyId;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "姓名不能为空")
    @DiffLogField(name = "姓名")
    private String name;

    @Schema(description = "部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "部门不能为空")
    @DiffLogField(name = "部门")
    private String depart;

    @Schema(description = "职务")
    @DiffLogField(name = "职务")
    private String position;

    @Schema(description = "在职状态：0-在职、1-离职", example = "1")
    private Integer status;

    @Schema(description = "直属上级，子表ID")
    @DiffLogField(name = "直属上级")
    private String superior;

    @Schema(description = "部门负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "部门负责人不能为空")
    @DiffLogField(name = "部门负责人")
    private Integer header;

    @Schema(description = "性别")
    @DiffLogField(name = "性别", function = SexParseFunction.NAME)
    private Integer sex;

    @Schema(description = "电话", example = "18000000000")
    @DiffLogField(name = "电话")
    @Telephone
    private String phone;

    @Schema(description = "邮箱", example = "123456789@qq.com")
    @DiffLogField(name = "邮箱")
    @Email
    private String email;

    @Schema(description = "年龄")
    @DiffLogField(name = "年龄")
    private Integer age;

    @Schema(description = "地址")
    @DiffLogField(name = "地址")
    private String address;

    @Schema(description = "备注", example = "你说的对")
    @DiffLogField(name = "备注")
    private String remark;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}